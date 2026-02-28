package org.example.placement_drive_management.service.Impl;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentResponseDto;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.StudentMapper;
import org.example.placement_drive_management.mappers.StudentResponseMapper;
import org.example.placement_drive_management.repository.StudentRepository;
import org.example.placement_drive_management.service.StudentService;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
private StudentRepository studentRepository;


@Override
    public StudentResponseDto createStudent(StudentDto studentDto) {
        Student newStudent= studentRepository.save(StudentMapper.maptoStudent(studentDto));
        return StudentResponseMapper.maptoStudentResponseDto(newStudent);
    }

    @Override
    public Student getStudentByRollNo(String rollNo) {
        return studentRepository.findByRollNo(rollNo).orElseThrow(()->new ResourceNotFoundException("Student with Roll No :"+rollNo+"not found"));
    }

    @Override
    public List<StudentDto> getAllStudents() {
        List<StudentDto> studentdto=studentRepository.findAll().stream().map((student)->StudentMapper.maptoStudentDto(student)).collect(Collectors.toList());
        return studentdto;
    }

}
