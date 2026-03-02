package org.example.placement_drive_management.controllers;

import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Applications;
import org.example.placement_drive_management.service.StudentProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/profile")
public class StudentProfileController {
    private final StudentProfileService studentProfileService;
    public  StudentProfileController(StudentProfileService studentProfileService) {
        this.studentProfileService = studentProfileService;
    }
    @PostMapping("/add/{rollNo}")
    public ResponseEntity<String> addStudentProfile(@PathVariable String rollNo, @RequestBody StudentProfileDto studentProfileDto) {
        String message = studentProfileService.createStudentProfile(rollNo,studentProfileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
    @PutMapping("/update/{rollNo}")
    public ResponseEntity<String> updateStudentProfile(@PathVariable String rollNo,@RequestBody StudentProfileDto studentProfileDto) {
        String message = studentProfileService.updateStudentProfile(rollNo,studentProfileDto);
        return ResponseEntity.ok(message);
    }
    @GetMapping("/{rollNo}")
    public ResponseEntity<StudentProfileDto> getStudentProfile(@PathVariable String rollNo) {
        StudentProfileDto studentProfileDto = studentProfileService.getStudentProfile(rollNo);
        return ResponseEntity.ok(studentProfileDto);
    }
    @GetMapping("/allApplications/{studentRollNo}")
    public  ResponseEntity<List<Applications>> getAllApplicationsOfStudent(@PathVariable String studentRollNo){
        return ResponseEntity.ok(studentProfileService.getAllApplicationsForStudent(studentRollNo));
    }
}
