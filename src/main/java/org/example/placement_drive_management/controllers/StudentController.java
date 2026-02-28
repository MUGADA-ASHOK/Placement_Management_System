package org.example.placement_drive_management.controllers;


import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentResponseDto;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.repository.StudentRepository;
import org.example.placement_drive_management.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class StudentController {
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public StudentController(StudentService studentService, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }
    @PostMapping("/register")
    public ResponseEntity<StudentResponseDto> registerStudent(@RequestBody StudentDto studentDto) {
        StudentResponseDto response = studentService.createStudent(studentDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allStudents")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto>  studentsDto=studentService.getAllStudents();
        return ResponseEntity.ok(studentsDto);
    }


}
