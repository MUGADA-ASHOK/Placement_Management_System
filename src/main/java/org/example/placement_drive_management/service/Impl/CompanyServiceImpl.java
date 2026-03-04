package org.example.placement_drive_management.service.Impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;

import org.example.placement_drive_management.entity.Applications;
import org.example.placement_drive_management.entity.Drive;
import org.example.placement_drive_management.entity.DriveRound;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.ApplicationsMapper;
import org.example.placement_drive_management.mappers.DriveMapper;
import org.example.placement_drive_management.mappers.DriveRoundMapper;
import org.example.placement_drive_management.repository.ApplicationRepository;
import org.example.placement_drive_management.repository.DriveRepository;
import org.example.placement_drive_management.repository.DriveRoundRepository;
import org.example.placement_drive_management.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private DriveRoundRepository driveRoundRepository;
    private DriveRepository driveRepository;
    private ApplicationRepository applicationRepository;
    @Override
    public String publishDriveRound(String driveId,DriveRoundDto driveRoundDto) {
        List<Applications> applications = applicationRepository.findByDrive_DriveId(driveId).orElseThrow(()-> new ResourceNotFoundException("Applications not found"));
        Drive drive = driveRepository.findByDriveId(driveId).orElseThrow(()-> new ResourceNotFoundException("Drive not found"));
        drive.getRounds().add(DriveRoundMapper.mapToDriveRound(driveRoundDto));
        DriveRound driveRound = DriveRoundMapper.mapToDriveRound(driveRoundDto);
        driveRound.setDrive(drive);
        driveRoundRepository.save(driveRound);
        for(Applications application:applications){
            if(!application.getStatus().equals("REJECTED")) {
                application.getDriveRounds().add(driveRound);
                driveRound.setApplications(application);
            }
        }
        return "Round "+driveRoundDto.getRoundNumber()+" has been published";
    }

    @Override
    public List<DriveDto> getAllDrives() {
        List<Drive>  drives = driveRepository.findAll();
        return drives.stream().map(DriveMapper::maptoDriveDto).collect(Collectors.toList());
    }

    @Override
    public List<DriveRoundDto> getAllRounds(String driveId) {
        List<DriveRound> driveRounds = driveRoundRepository.findAll();
        return driveRounds.stream().map(DriveRoundMapper::mapToDriveRoundDto).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationsDto> getAllApplications(String driveId) {
        List<Applications> applications = applicationRepository.findByDrive_DriveId(driveId).orElseThrow(()-> new ResourceNotFoundException("Applications not found"));
        return applications.stream().map(ApplicationsMapper::mapToApplicationDto).collect(Collectors.toList());
    }

    @Override
    public List<ApplicationsDto> getApplicantsForDriveRound(String driveId, Integer roundNo) {

        List<Applications> applications =
                applicationRepository.findApplicantsByDriveIdAndRoundNo(driveId, roundNo);

        return applications.stream()
                .map(ApplicationsMapper::mapToApplicationDto)
                .toList();
    }

}
