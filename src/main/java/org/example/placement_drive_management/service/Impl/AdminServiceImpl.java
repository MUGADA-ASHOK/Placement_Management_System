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
    public StudentProfileDto getStudentProfileByRollNo(String rollNo) {
        StudentProfile studentProfile = studentProfileRepository.findByStudentRollNo(rollNo).orElseThrow(()->new ResourceNotFoundException("Student with RollNo"+rollNo+" need to be uploaded"));
        return StudentProfileMapper.maptoStudentProfileDto(studentProfile);
    }

    @Override
    public DriveDto createDrive(DriveDto driveDto) {
        if(driveRepository.existsByDriveId(driveDto.getDriveId())) {
            return driveDto;
        }
        Drive newdrive=DriveMapper.maptoDrive(driveDto);
        Company company=companyRepository.findByCompanyId(driveDto.getCompanyId()).orElseThrow(()->new ResourceNotFoundException("Company with id"+driveDto.getCompanyId()+" need to be uploaded"));
        newdrive.setCompany(company);
        driveRepository.save(newdrive);
        return DriveMapper.maptoDriveDto(newdrive);
    }


    @Override
    public String createEligibility(EligibilityDto eligibilityDto) {
        Drive drive = driveRepository.findByDriveId(eligibilityDto.getDriveId()).orElseThrow(()-> new ResourceNotFoundException("drive not found"));
        if(driveRepository.existsByDriveId(drive.getDriveId())) {
            return "drive already exists";
        }
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
        return "Eligiblity created successfully";

    }
    @Override
    public String updateEligibility(String driveId,EligibilityDto eligibilityDto) {
            Drive drive=driveRepository.findByDriveId(driveId).orElseThrow(() -> new ResourceNotFoundException("Drive not found"));
            if (!drive.getRounds().isEmpty()) {
                return "Eligibility cannot be updated once rounds are added";
            }
            Eligibility updatedEligibility=eligibilityRepository.findByDrive_DriveId(driveId).orElseThrow(() -> new ResourceNotFoundException("Eligibility not found"));
            updatedEligibility.setMinimumCgpa(eligibilityDto.getMinimumCgpa());
            updatedEligibility.setMaxActiveBacklogs(eligibilityDto.getMaxActiveBacklogs());
            updatedEligibility.setAllowedBranch(eligibilityDto.getAllowedBranch());
            updatedEligibility.setPassingYear(eligibilityDto.getPassingYear());
            updatedEligibility.setGender(eligibilityDto.getGender());
            updatedEligibility.setHasHistoryBacklogs(eligibilityDto.getHasHistoryBacklogs());
            eligibilityRepository.save(updatedEligibility);
            drive.setEligibility(updatedEligibility);
            if(!drive.getRounds().isEmpty()){
                List<Applications> applications=drive.getApplications();
                for(Applications application:applications){
                    applicationRepository.delete(application);
                }
                if(drive.getIsActive()) {
                    drive.setIsActive(false);
                    driveRepository.save(drive);
                    return publishDrivesToEligibleStudents(driveId);
                }
            }
            return "Successfully updated Eligibility";
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
    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company>companies = companyRepository.findAll();
        return companies.stream().map(company -> CompanyMapper.mapCompanyToDto(company)).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationsDto> getAllApplicationsForaStudent(String rollNo) {
        List<Applications> applications = applicationRepository.findByStudent_RollNo(rollNo);
        return applications.stream().map(ApplicationsMapper::mapToApplicationDto).collect(Collectors.toList());
    }

    @Override
    public String closeDrive(String driveId) {
        Drive drive = driveRepository.findByDriveId(driveId).orElseThrow(()-> new ResourceNotFoundException("Drive not found"));
        drive.setIsActive(false);
        driveRepository.save(drive);
        return "Drive closed successfully";
    }

    @Override
    public List<DriveRoundDto> getAllRounds(String driveId){
        List<DriveRound> driveRounds = driveRoundRepository
                .findByDrive_DriveId(driveId);
        return driveRounds.stream()
                .map(DriveRoundMapper::mapToDriveRoundDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationsDto> getAllApplications(String driveId) {
        List<Applications> applications = applicationRepository
                .findByDrive_DriveId(driveId);
        if (applications.isEmpty()) {
            return Collections.emptyList();
        }
        return applications.stream()
                .map(ApplicationsMapper::mapToApplicationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationRoundDto> getApplicantsForDriveRound(String driveId, Integer roundNo) {

        List<ApplicationRound> roundEntries = applicationRoundRepository.findDetailedRoundApplicants(driveId, roundNo);
        if (roundEntries.isEmpty()) {
            return Collections.emptyList();
        }
        return roundEntries.stream()
                .map(ApplicationRoundMapper::maptoApplicationRoundDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriveDto> viewAllActiveDrives() {
        List<Drive> drives=driveRepository.findAllByIsActive(true);
        return drives.stream().map(DriveMapper::maptoDriveDto).collect(Collectors.toList());
    }

    @Override
    public String removeDrive(String driveId) {
        Drive drive=driveRepository.findByDriveId(driveId).orElseThrow(() -> new ResourceNotFoundException("drive not found"));
        if(drive.getIsActive()){
            return "Drive is Already Published ,You cannot remove Now";
        }
        driveRepository.delete(drive);
        return "Drive has been removed successfully";
    }
    @Override
    public String extendDriveApplication(String driveId, LocalDate localDate) {
        Drive drive=driveRepository.findByDriveId(driveId).orElseThrow(() -> new ResourceNotFoundException("drive not found"));
        if(drive.getRounds().size()==0 && localDate.isBefore(drive.getRegistrationEndDate())){
            drive.setRegistrationEndDate(localDate);
            return "Registration date has been extended to "+localDate;
        }
        return "You cannot update the Registration Date now";
    }

}
