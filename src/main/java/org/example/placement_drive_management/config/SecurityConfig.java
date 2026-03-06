package org.example.placement_drive_management.config;

import lombok.RequiredArgsConstructor;
import org.example.placement_drive_management.filter.JwtAuthFilter;
import org.example.placement_drive_management.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)   // enables @PreAuthorize on controller methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ── Disable CSRF — we are stateless (JWT), no sessions ────────
                .csrf(AbstractHttpConfigurer::disable)

                // ── Custom JSON error responses ───────────────────────────────
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)   // 401
                        .accessDeniedHandler(customAccessDeniedHandler)   // 403
                )

                // ── No sessions — every request must carry a JWT ──────────────
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ── Endpoint authorization rules ──────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        // Public — no token required
                        .requestMatchers("/api/auth/**").permitAll()

                        // Role-restricted paths (method-level @PreAuthorize is the final gate)
                        .requestMatchers("/api/student/**").hasAuthority("ROLE_STUDENT")
                        .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN")
                        .requestMatchers("/api/company/**").hasAuthority("ROLE_COMPANY")
                        .requestMatchers("/api/super-admin/**").hasAuthority("ROLE_SUPER_ADMIN")

                        // Everything else needs to be authenticated
                        .anyRequest().authenticated()
                )

                // ── Use our custom DaoAuthenticationProvider ──────────────────
                .authenticationProvider(authenticationProvider())

                // ── JWT filter runs BEFORE Spring's username/password filter ──
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}