package org.example.placement_drive_management.controllers;


import lombok.Getter;
import org.example.placement_drive_management.dto.*;
import org.example.placement_drive_management.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminControllers {
    private final AdminService adminService;
    public AdminControllers(AdminService adminService) {

        this.adminService = adminService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDto adminDto) {
        adminService.registerAdmin(adminDto);
        return ResponseEntity.ok("admin registered successfully");
    }

    @GetMapping("/allStudents")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(adminService.getAllStudents());
    }
    @GetMapping("/allStudentProfiles")
    public ResponseEntity<List<StudentProfileDto>> getAllStudentProfiles() {
        return ResponseEntity.ok(adminService.getAllProfiles());
    }
    @GetMapping("/student/{rollNo}/profile")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable("rollNo") String rollNo) {
        return ResponseEntity.ok(adminService.getStudentProfileByRollNo(rollNo));
    }
    @PostMapping("/company/register")
    public ResponseEntity<CompanyDto> registerCompany(@RequestBody CompanyDto companyDto) {
         CompanyDto company = adminService.registerCompany(companyDto);
         return ResponseEntity.ok(company);
    }
    @PostMapping("/company/addDrive")
    public ResponseEntity<DriveDto> addDrive(@RequestBody DriveDto driveDto) {
        return ResponseEntity.ok(adminService.createDrive(driveDto));
    }
    @PostMapping("/company/addDriveEligibility")
    public ResponseEntity<EligibilityDto> createEligibility(@RequestBody EligibilityDto eligibilityDto) {
            return ResponseEntity.ok(adminService.createEligibility(eligibilityDto));
    }
    @PutMapping("/publishDrives/{driveId}")
    public ResponseEntity<String> publishDrives(@PathVariable("driveId") String driveId) {
        return ResponseEntity.ok(adminService.publishDrivesToEligibleStudents(driveId));
    }
    @GetMapping("/company/{companyId}/drives")
    public ResponseEntity<List<DriveDto>> getAllDrives(@PathVariable("companyId") String companyId) {
        return ResponseEntity.ok(adminService.getAllDrives(companyId));
    }

}
