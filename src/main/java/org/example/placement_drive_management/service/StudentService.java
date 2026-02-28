package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentResponseDto;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.repository.StudentRepository;

import java.util.List;

public interface StudentService {
   /* StudentResponseDto getStudentByRollNo(String rollNo);
    StudentResponseDto getStudentById(Long id);*/
    StudentResponseDto createStudent(StudentDto studentDto);
    public Student getStudentByRollNo(String rollNo);
    List<StudentDto> getAllStudents();
}

