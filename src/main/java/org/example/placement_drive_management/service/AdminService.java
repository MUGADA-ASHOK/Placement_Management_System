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
    StudentProfileDto getStudentProfileByRollNo(String rollNo);
    DriveDto createDrive(DriveDto driveDto);
    Company findCompanyById(String companyId);
    EligibilityDto createEligibility(EligibilityDto eligibilityDto);
    EligibilityDto updateEligibility(EligibilityDto eligibilityDto);
    List<StudentProfileDto> getAllProfiles();
    String publishDrivesToEligibleStudents(String driveId);
    List<DriveDto> getAllDrives(String companyId);
    List<CompanyDto> getAllCompanies();
    List<ApplicationsDto> getAllApplicationsForaStudent(String rollNo);
}
