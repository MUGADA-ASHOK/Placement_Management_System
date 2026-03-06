package org.example.placement_drive_management.controllers;

import lombok.Getter;
import org.example.placement_drive_management.dto.*;
import org.example.placement_drive_management.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")   // ← FIXED: was /api/auth/admin
public class AdminControllers {

    private final AdminService adminService;

    public AdminControllers(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/allStudents")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }

    @GetMapping("/allStudentProfiles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<StudentProfileDto>> getAllStudentProfiles() {
        return ResponseEntity.ok(adminService.getAllProfiles());
    }

    @GetMapping("/student/{rollNo}/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable("rollNo") String rollNo) {
        return ResponseEntity.ok(adminService.getStudentProfileByRollNo(rollNo));
    }

    @PostMapping("/company/addDrive")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DriveDto> addDrive(@RequestBody DriveDto driveDto) {
        return ResponseEntity.ok(adminService.createDrive(driveDto));
    }

    @PostMapping("/company/addDriveEligibility")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<EligibilityDto> createEligibility(@RequestBody EligibilityDto eligibilityDto) {
        return ResponseEntity.ok(adminService.createEligibility(eligibilityDto));
    }

    @PutMapping("/publishDrives/{driveId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<String> publishDrives(@PathVariable("driveId") String driveId) {
        return ResponseEntity.ok(adminService.publishDrivesToEligibleStudents(driveId));
    }

    @GetMapping("/company/{companyId}/drives")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<DriveDto>> getAllDrives(@PathVariable("companyId") String companyId) {
        return ResponseEntity.ok(adminService.getAllDrives(companyId));
    }
}