package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.*;
import org.example.placement_drive_management.entity.Company;
import org.example.placement_drive_management.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface AdminService {
    List<StudentDto> getAllStudents();
    String registerAdmin(AdminDto adminDto);
    StudentProfileDto getStudentProfileByRollNo(String rollNo);
    CompanyDto registerCompany(CompanyDto companyDto);
    DriveDto createDrive(DriveDto driveDto);
    Company findCompanyById(String companyId);
    EligibilityDto createEligibility(EligibilityDto eligibilityDto);
    String addDriveRound(DriveRoundDto driveRoundDto);
    List<StudentProfileDto> getAllProfiles();
    String publishDrivesToEligibleStudents(String driveId);
}
