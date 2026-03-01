package org.example.placement_drive_management.service.Impl;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.*;
import org.example.placement_drive_management.entity.*;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.*;
import org.example.placement_drive_management.repository.*;
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
    private CompanyRepository companyRepository;
    private DriveRepository driveRepository;
    private EligibilityRepository eligibilityRepository;
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

    @Override
    public CompanyDto registerCompany(CompanyDto companyDto) {
       return CompanyMapper.mapToCompanyDto(companyRepository.save(CompanyMapper.mapToCompany(companyDto)));

    }

    @Override
    public DriveDto createDrive(DriveDto driveDto) {
        Drive newdrive=DriveMapper.maptoDrive(driveDto);
        Company company=companyRepository.findByCompanyId(driveDto.getCompanyId()).orElseThrow(()->new ResourceNotFoundException("Company with id"+driveDto.getCompanyId()+" need to be uploaded"));
        newdrive.setCompany(company);
        driveRepository.save(newdrive);
        return DriveMapper.maptoDriveDto(newdrive);
    }

    @Override
    public Company findCompanyById(String companyId) {
        return companyRepository.findByCompanyId(companyId).orElseThrow(()->new ResourceNotFoundException("Company with id"+companyId+" need to be uploaded"));
    }

    @Override
    public EligibilityDto createEligibility(EligibilityDto eligibilityDto) {
        Drive drive = driveRepository.findByDriveId(eligibilityDto.getDriveId()).orElseThrow(()-> new ResourceNotFoundException("drive not found"));
        Eligibility eligibility=new  Eligibility(
                eligibilityDto.getId(),
                eligibilityDto.getMinimumCgpa(),
                eligibilityDto.getMaxActiveBacklogs(),
                eligibilityDto.getAllowedBranch(),
                eligibilityDto.getPassingYear(),
                eligibilityDto.getGender(),
                drive
        );
        drive.setEligibility(eligibility);
        eligibilityRepository.save(eligibility);

        return EligibilityMapper.mapToEligibilityDto(eligibility);

    }
}
