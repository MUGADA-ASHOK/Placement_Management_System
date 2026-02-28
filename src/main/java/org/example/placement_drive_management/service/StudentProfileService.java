package org.example.placement_drive_management.service;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.springframework.stereotype.Service;

@Service
public interface StudentProfileService {
    String createStudentProfile(String rollNo, StudentProfileDto studentProfileDto);
    String updateStudentProfile(String RollNo,StudentProfileDto studentProfileDto);
    StudentProfileDto getStudentProfile(String rollNo);
}
