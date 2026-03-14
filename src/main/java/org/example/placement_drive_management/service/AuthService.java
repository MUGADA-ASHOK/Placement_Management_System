package org.example.placement_drive_management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.placement_drive_management.dto.auth.*;
import org.example.placement_drive_management.entity.Admin;
import org.example.placement_drive_management.entity.Company;
import org.example.placement_drive_management.entity.RefreshToken;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.enums.Role;
import org.example.placement_drive_management.exceptions.DuplicateEmailException;
import org.example.placement_drive_management.repository.AdminRepository;
import org.example.placement_drive_management.repository.CompanyRepository;
import org.example.placement_drive_management.repository.StudentRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;
    private final CustomUserDetailsService userDetailsService;
    private final CompanyRepository companyRepository;
    // ── Register: Student ────────────────────────────────────────

    @Transactional
    public AuthResponse registerStudent(RegisterStudentRequest request) {
        checkEmailNotTaken(request.getEmail());

        Student student = new Student();
        student.setRollNo(request.getRollNo());
        student.setName(request.getName());
        student.setSurname(request.getSurname());
        student.setEmail(request.getEmail());
        student.setMobileNo(request.getMobileNo());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRole(Role.ROLE_STUDENT);

        studentRepository.save(student);
        log.info("Registered new student: {}", request.getEmail());

        return buildAuthResponse(student, Role.ROLE_STUDENT, request.getDeviceInfo());
    }

    // ── Register: Admin ──────────────────────────────────────────

    @Transactional
    public AuthResponse registerAdmin(RegisterAdminRequest request) {
        checkEmailNotTaken(request.getEmail());

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.ROLE_ADMIN);

        adminRepository.save(admin);
        log.info("Registered new admin: {}", request.getEmail());

        return buildAuthResponse(admin, Role.ROLE_ADMIN, request.getDeviceInfo());
    }
    @Transactional
    public AuthResponse registerCompany(RegisterCompanyRequest request) {
        checkEmailNotTaken(request.getEmail());

        Company company = new Company();
        company.setCompanyId(request.getCompanyId());
        company.setCompanyName(request.getCompanyName());
        company.setEmail(request.getEmail());
        company.setPassword(passwordEncoder.encode(request.getPassword()));
        company.setWebsite(request.getWebsite());
        company.setIndustryType(request.getIndustryType());
        company.setDescription(request.getDescription());
        company.setExternalApplicationLink(request.getExternalApplicationLink());
        company.setRole(Role.ROLE_COMPANY);

        companyRepository.save(company);
        log.info("Registered new company: {}", request.getEmail());

        return buildAuthResponse(company, Role.ROLE_COMPANY, request.getDeviceInfo());
    }


    // ── Login ────────────────────────────────────────────────────

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // AuthenticationManager verifies credentials — throws BadCredentialsException on failure
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Role role = resolveRole(userDetails);

        log.info("User logged in: {} [{}]", request.getEmail(), role);
        return buildAuthResponse(userDetails, role, request.getDeviceInfo());
    }
    @Transactional
    public AuthResponse companyLogin(LoginRequest request) {
        // AuthenticationManager verifies credentials — throws BadCredentialsException on failure
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Role role = resolveRole(userDetails);

        log.info("User logged in: {} [{}]", request.getEmail(), role);
        return buildAuthResponse(userDetails, role, request.getDeviceInfo());
    }
    @Transactional
    public AuthResponse adminLogin(LoginRequest request) {
        // AuthenticationManager verifies credentials — throws BadCredentialsException on failure
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        Role role = resolveRole(userDetails);

        log.info("User logged in: {} [{}]", request.getEmail(), role);
        return buildAuthResponse(userDetails, role, request.getDeviceInfo());
    }

    // ── Refresh ──────────────────────────────────────────────────

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request) {
        // 1. Verify old refresh token (throws if expired/revoked/missing)
        RefreshToken oldToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        // 2. Re-load user from DB → picks up any role change since last login
        UserDetails userDetails = userDetailsService.loadUserByUsername(oldToken.getEmail());
        Role currentRole = resolveRole(userDetails);

        // 3. Rotate: revoke old token, issue new one (prevents replay)
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldToken);

        // 4. Issue new access token with current (DB-authoritative) role
        String newAccessToken = jwtService.generateAccessToken(userDetails, currentRole);

        log.info("Tokens rotated for: {}", oldToken.getEmail());
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .email(userDetails.getUsername())
                .role(currentRole)
                .build();
    }

    // ── Logout ───────────────────────────────────────────────────

    @Transactional
    public void logout(LogoutRequest request, String rawAccessToken) {
        // 1. Revoke the refresh token
        refreshTokenService.revokeToken(request.getRefreshToken());

        // 2. Blacklist the current access token until it naturally expires
        if (rawAccessToken != null && !rawAccessToken.isBlank()) {
            try {
                Date expiry = jwtService.extractExpiration(rawAccessToken);
                tokenBlacklistService.blacklist(rawAccessToken, expiry.toInstant());
            } catch (Exception e) {
                // Token may already be expired — blacklist is best-effort
                log.debug("Could not blacklist access token on logout: {}", e.getMessage());
            }
        }

        log.info("User logged out successfully.");
    }

    // ── Helpers ──────────────────────────────────────────────────

    private AuthResponse buildAuthResponse(UserDetails userDetails, Role role, String deviceInfo) {
        String accessToken  = jwtService.generateAccessToken(userDetails, role);
        RefreshToken rt     = refreshTokenService.createRefreshToken(
                userDetails.getUsername(), role.name(), deviceInfo);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(rt.getToken())
                .tokenType("Bearer")
                .accessTokenExpiresIn(jwtService.getAccessTokenExpirationMs() / 1000)
                .email(userDetails.getUsername())
                .role(role)
                .build();
    }

    private Role resolveRole(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> Role.valueOf(a.getAuthority()))
                .orElseThrow(() -> new IllegalStateException("User has no role assigned."));
    }

    private void checkEmailNotTaken(String email) {
        boolean taken = studentRepository.existsByEmail(email)
                || adminRepository.existsByEmail(email)
                || companyRepository.existsByEmail(email); // ← ADD THIS
        if (taken) {
            throw new DuplicateEmailException("Email is already registered: " + email);
        }
    }
}