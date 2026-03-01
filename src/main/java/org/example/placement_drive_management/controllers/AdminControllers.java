package org.example.placement_drive_management.controllers;


import org.example.placement_drive_management.dto.AdminDto;
import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
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
    @GetMapping("/student/{rollNo}/profile")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable("rollNo") String rollNo) {
        return ResponseEntity.ok(adminService.getStudentProfileByRollNo(rollNo));
    }
}
