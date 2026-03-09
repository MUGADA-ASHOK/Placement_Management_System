package org.example.placement_drive_management.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.ApplicationRoundDto;
import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.entity.ApplicationRound;
import org.example.placement_drive_management.entity.Applications;
import org.example.placement_drive_management.entity.Drive;
import org.example.placement_drive_management.entity.DriveRound;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.ApplicationRoundMapper;
import org.example.placement_drive_management.mappers.ApplicationsMapper;
import org.example.placement_drive_management.mappers.DriveMapper;
import org.example.placement_drive_management.mappers.DriveRoundMapper;
import org.example.placement_drive_management.repository.ApplicationRepository;
import org.example.placement_drive_management.repository.ApplicationRoundRepository;
import org.example.placement_drive_management.repository.DriveRepository;
import org.example.placement_drive_management.repository.DriveRoundRepository;
import org.example.placement_drive_management.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private DriveRoundRepository driveRoundRepository;
    private DriveRepository driveRepository;
    private ApplicationRepository applicationRepository;
    private ApplicationRoundRepository applicationRoundRepository;

    public String publishDriveRound(String driveId, DriveRoundDto dto) {

        // 1. Validate drive exists
        Drive drive = driveRepository.findByDriveId(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found: " + driveId));

        // 2. Prevent duplicate round numbers for same drive
        Boolean roundExists = driveRoundRepository
                .existsByDrive_DriveIdAndRoundNumber(driveId, dto.getRoundNumber());
        if (roundExists) {
            return "Round " + dto.getRoundNumber() + " already exists for this drive.";
        }

        // 3. Create and save DriveRound first
        DriveRound driveRound = DriveRoundMapper.mapToDriveRound(dto);
        driveRound.setDrive(drive);
        driveRoundRepository.save(driveRound);

        // 4. Fetch only the relevant applications directly from DB
        // Round 1 → fetch only APPLIED students
        // Round 2,3,4... → fetch only INPROCESS students (cleared previous round)
            String requiredStatus = (dto.getRoundNumber() == 1) ? "APPLIED" : "INPROCESS";

        List<Applications> eligibleApplications = applicationRepository
                .findByDrive_DriveIdAndStatus(driveId, requiredStatus);
        // DB does the filtering — no loading of rejected/selected students at all

        if (eligibleApplications.isEmpty()) {
            return "No eligible students found for round " + dto.getRoundNumber();
        }

        // 5. Create ApplicationRound for each eligible student
        List<ApplicationRound> applicationRounds = new ArrayList<>();
        for (Applications application : eligibleApplications) {
            ApplicationRound applicationRound = new ApplicationRound();
            applicationRound.setDriveRound(driveRound);
            applicationRound.setApplication(application);
            applicationRound.setStatus("PENDING"); // default status
            applicationRounds.add(applicationRound);
        }

        // 6. Batch save — one DB call instead of N calls
        applicationRoundRepository.saveAll(applicationRounds);

        return "Round " + dto.getRoundNumber() + " published for "
                + eligibleApplications.size() + " students successfully.";
    }
    @Override
    public List<DriveDto> getAllDrives() {
        List<Drive> drives = driveRepository.findAll();
        return drives.stream()
                .map(DriveMapper::maptoDriveDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DriveRoundDto> getAllRounds(String driveId) {
        List<DriveRound> driveRounds = driveRoundRepository.findByDrive_DriveId(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Rounds not found for drive: " + driveId));
        return driveRounds.stream()
                .map(DriveRoundMapper::mapToDriveRoundDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationsDto> getAllApplications(String driveId) {
        List<Applications> applications = applicationRepository.findByDrive_DriveId(driveId);
        if(applications.isEmpty()) {
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
            return  Collections.emptyList();
        }
        return roundEntries.stream().map(ApplicationRoundMapper::maptoApplicationRoundDto).collect(Collectors.toList());
    }
    @Override
    public String publishScoreForDriveRound(String driveId,String rollNo,Integer roundNo,Double score){
        Applications application = applicationRepository.findByDrive_DriveIdAndStudent_RollNo(driveId,rollNo).orElseThrow(() -> new ResourceNotFoundException("Applications not found"));
        ApplicationRound applicationRound = applicationRoundRepository.findByApplication_IdAndDriveRound_RoundNumber(application.getId(), roundNo).orElseThrow(() -> new ResourceNotFoundException("ApplicationRound not found"));
        applicationRound.setScore(score);
        applicationRound.setStatus("PENDING");
        applicationRoundRepository.save(applicationRound);
        return "Score Successfully published to RollNo: "+rollNo;
    }

    @Override
    public String filterTopKStudents(String driveId,Integer roundNo,Integer topK) {
        List<ApplicationRound> studentApplications = applicationRoundRepository.findScoredStudentsOrderByScore(driveId,roundNo);
        if (studentApplications.isEmpty()) {
            throw new ResourceNotFoundException("No students found for this round");
        }
        if (topK<=0) {
            throw new ResourceNotFoundException("Top K students not found for this round");
        }
        for(int i=0;i<topK && i<studentApplications.size();i++){
            studentApplications.get(i).setStatus("CLEARED");
            applicationRoundRepository.save(studentApplications.get(i));
            Applications application = studentApplications.get(i).getApplication();
            application.setStatus("INPROCESS");
            applicationRepository.save(application);
        }
        for(int i=topK;i<studentApplications.size();i++){
            studentApplications.get(i).setStatus("FAILED");
            applicationRoundRepository.save(studentApplications.get(i));
            Applications application = studentApplications.get(i).getApplication();
            application.setStatus("REJECTED");
            applicationRepository.save(application);
        }
        //applicationRoundRepository.saveAll(studentApplications);
        return "status updated successfully";
    }

    @Override
    public String filterByCutOffMarks(String driveId, Integer roundNo,Double cutOff) {
        List<ApplicationRound> studentApplications = applicationRoundRepository.findScoredStudentsOrderByScore(driveId,roundNo);
        for(ApplicationRound applicationRound : studentApplications){
            if(applicationRound.getScore()>=cutOff){
                applicationRound.setStatus("CLEARED");
                applicationRoundRepository.save(applicationRound);
                Applications application = applicationRound.getApplication();
                application.setStatus("INPROCESS");
                applicationRepository.save(application);
            }
            else{
                applicationRound.setStatus("FAILED");
                applicationRoundRepository.save(applicationRound);
                Applications application = applicationRound.getApplication();
                application.setStatus("REJECTED");
                applicationRepository.save(application);
            }

        }

        //applicationRoundRepository.saveAll(studentApplications);
        return "status updated successfully";
    }


}