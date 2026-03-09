package org.example.placement_drive_management.controllers;

import org.example.placement_drive_management.dto.ApplicationRoundDto;
import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.service.StudentProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student/profile")   // ← FIXED: was /api/auth/profile
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    public StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }

    @PostMapping("/add/{rollNo}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> addStudentProfile(
            @PathVariable String rollNo,
            @RequestBody StudentProfileDto studentProfileDto) {
        String message = studentProfileService.createStudentProfile(rollNo, studentProfileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PutMapping("/update/{rollNo}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> updateStudentProfile(
            @PathVariable String rollNo,
            @RequestBody StudentProfileDto studentProfileDto) {
        String message = studentProfileService.updateStudentProfile(rollNo, studentProfileDto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{rollNo}")
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable String rollNo) {
        return ResponseEntity.ok(studentProfileService.getStudentProfile(rollNo));
    }

    @GetMapping("/allApplications/{rollNo}")
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<ApplicationsDto>> getAllApplicationsOfStudent(
            @PathVariable("rollNo") String studentRollNo) {
        return ResponseEntity.ok(studentProfileService.getAllApplicationsForStudent(studentRollNo));
    }

    @PostMapping("/applyDrive/{driveId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> applyDrive(@PathVariable String driveId, @AuthenticationPrincipal Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentProfileService.applyDrive(driveId,student));
    }


}