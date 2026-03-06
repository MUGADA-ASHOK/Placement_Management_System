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

    @Override
    public String publishDriveRound(String driveId, DriveRoundDto dto) {
        Drive drive = driveRepository.findByDriveId(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Drive not found"));

        if (driveRoundRepository.existsByDriveAndRoundNumber(drive, dto.getRoundNumber())) {
            throw new IllegalStateException("Round already exists");
        }

        //  Create and set drive (VERY IMPORTANT)
        DriveRound driveRound = DriveRoundMapper.mapToDriveRound(dto);
        driveRound.setDrive(drive);  //  REQUIRED
        driveRoundRepository.save(driveRound);

        List<Applications> applications;

        // First Round
        if (dto.getRoundNumber() == 1) {

            applications = applicationRepository
                    .findByDrive_DriveId(driveId)
                    .orElseThrow(() -> new ResourceNotFoundException("Applications not found"));
        }
        //  Next Rounds
        else {

            applications = applicationRoundRepository
                    .findClearedApplicationsFromPreviousRound(
                            driveId,
                            dto.getRoundNumber() - 1,
                            "CLEARED"
                    );
        }

        //  Create ApplicationRound entries
        List<ApplicationRound> roundEntries = applications.stream()
                .map(app -> {
                    ApplicationRound ar = new ApplicationRound();
                    ar.setApplication(app);
                    ar.setDriveRound(driveRound);
                    ar.setStatus("PENDING");
                    return ar;
                })
                .toList();

        applicationRoundRepository.saveAll(roundEntries);

        return "Drive round added successfully to " + roundEntries.size() + " applicants";
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
        List<Applications> applications = applicationRepository.findByDrive_DriveId(driveId)
                .orElseThrow(() -> new ResourceNotFoundException("Applications not found"));
        return applications.stream()
                .map(ApplicationsMapper::mapToApplicationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApplicationRoundDto> getApplicantsForDriveRound(String driveId, Integer roundNo) {
        List<ApplicationRound> roundEntries = applicationRoundRepository.findDetailedRoundApplicants(driveId, roundNo);
        if (roundEntries.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No applicants found for this round");
        }
        return roundEntries.stream().map(ApplicationRoundMapper::maptoApplicationRoundDto).collect(Collectors.toList());
    }
    @Override
    public String publishScoreForDriveRound(String driveId,String rollNo,Integer roundNo,Double score){

        return "Score Successfully published to RollNo: "+rollNo;
    }

}