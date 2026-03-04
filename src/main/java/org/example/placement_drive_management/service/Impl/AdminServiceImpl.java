package org.example.placement_drive_management.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.*;
import org.example.placement_drive_management.entity.*;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.*;
import org.example.placement_drive_management.repository.*;
import org.example.placement_drive_management.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;
    private StudentRepository studentRepository;
    private StudentProfileRepository studentProfileRepository;
    private CompanyRepository companyRepository;
    private DriveRepository driveRepository;
    private EligibilityRepository eligibilityRepository;
    private DriveRoundRepository driveRoundRepository;
    private ApplicationRepository applicationRepository;
    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream().map((students  )-> StudentMapper.maptoStudentDto(students)).collect(Collectors.toList());
    }
    @Override
    public List<StudentProfileDto> getAllProfiles() {
        return studentProfileRepository.findAll().stream().map(StudentProfileMapper::maptoStudentProfileDto).collect(Collectors.toList());
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
                eligibilityDto.getHasHistoryBacklogs(),
                drive
        );
        drive.setEligibility(eligibility);
        eligibilityRepository.save(eligibility);
        return EligibilityMapper.mapToEligibilityDto(eligibility);

    }

    @Override
    public String publishDrivesToEligibleStudents(String driveId) {
        int count=0;
        Drive drive=driveRepository.findByDriveId(driveId).orElseThrow(()-> new ResourceNotFoundException("drive not found"));
        if(drive.getIsActive()) {
            return "Eligible student has been published";
        }
        drive.setIsActive(true);
        Eligibility eligibility=eligibilityRepository.findByDrive_DriveId(driveId).orElseThrow(()->new ResourceNotFoundException("Drive Not Found"));
        List<StudentProfile> profiles = studentProfileRepository.findAll();
        for(StudentProfile profile:profiles){
            if(     eligibility.getAllowedBranch().contains(profile.getDepartment().toString()) &&
                    eligibility.getPassingYear().equals(profile.getPassingYear()) &&
                            eligibility.getMinimumCgpa()<= profile.getCurrentCgpa() &&
                            eligibility.getMaxActiveBacklogs()>=profile.getBacklogCount() &&
                            ( eligibility.getGender().equals("BOTH") || eligibility.getGender().equals(profile.getGender())) &&
                    (eligibility.getHasHistoryBacklogs() || !profile.getHasbackloghistory() )
                    ){
                  Applications application=new Applications();
                  application.setStudent(profile.getStudent());
                  application.setStudentProfile(profile);
                  application.setDrive(drive);
                  application.setStatus("ELIGIBLE");
                  drive.getApplications().add(application);
                  profile.getApplicationsList().add(application);
                  count+=1;
                  applicationRepository.save(application);
            }
        }
        return "Drives published successfully for  "+count+ " students";
    }

    @Override
    public List<DriveDto> getAllDrives(String companyId) {
        List<Drive> drives=companyRepository.findByCompanyId(companyId).orElseThrow(()->new ResourceNotFoundException("Comapny with companyId "+companyId +" not found")).getDrives();
        return drives.stream().map(DriveMapper::maptoDriveDto).collect(Collectors.toList());
    }
}
