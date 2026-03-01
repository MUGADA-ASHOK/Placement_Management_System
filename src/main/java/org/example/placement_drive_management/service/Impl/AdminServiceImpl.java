package org.example.placement_drive_management.service.Impl;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.AdminDto;
import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Admin;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.entity.StudentProfile;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.AdminMapper;
import org.example.placement_drive_management.mappers.StudentMapper;
import org.example.placement_drive_management.mappers.StudentProfileMapper;
import org.example.placement_drive_management.mappers.StudentResponseMapper;
import org.example.placement_drive_management.repository.AdminRepository;
import org.example.placement_drive_management.repository.StudentProfileRepository;
import org.example.placement_drive_management.repository.StudentRepository;
import org.example.placement_drive_management.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;
    private StudentRepository studentRepository;
    private StudentProfileRepository studentProfileRepository;
    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map((students  )-> StudentMapper.maptoStudentDto(students)).collect(Collectors.toList());
    }

    @Override
    public String registerAdmin(AdminDto adminDto) {
         adminRepository.save(AdminMapper.maptoAdmin(adminDto));
         return "admin registered successfully";
    }

    @Override
    public StudentProfileDto getStudentProfileByRollNo(String rollNo) {
        StudentProfile studentProfile = studentProfileRepository.findByStudentRollNo(rollNo).orElseThrow(()->new ResourceNotFoundException("Student with RollNo"+rollNo+" need to be uploaded"));
        return StudentProfileMapper.maptoStudentProfileDto(studentProfile);
    }
}
