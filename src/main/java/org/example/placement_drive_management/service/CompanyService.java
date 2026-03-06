package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.entity.Drive;

import java.util.List;


public interface CompanyService {
    String publishDriveRound(String driveId,DriveRoundDto driveRoundDto);

    List<DriveDto> getAllDrives();
    List<DriveRoundDto> getAllRounds(String driveId);
    List<ApplicationsDto>getAllApplications(String driveId);
    List<ApplicationsDto>getApplicantsForDriveRound(String driveId,Integer roundNo);
    String publishScoreForDriveRound(String driveId,String rollNo,Integer roundNo,Double score);
}
