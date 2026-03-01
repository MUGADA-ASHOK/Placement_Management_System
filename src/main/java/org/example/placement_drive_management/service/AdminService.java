package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.AdminDto;
import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface AdminService {
    List<StudentDto> getAllStudents();
    String registerAdmin(AdminDto adminDto);
    StudentProfileDto getStudentProfileByRollNo(String rollNo);
}
