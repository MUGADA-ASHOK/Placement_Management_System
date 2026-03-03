package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;

import java.util.List;


public interface CompanyService {
    String publishDriveRound(String driveId,DriveRoundDto driveRoundDto);

    List<DriveDto> getAllDrives();
    List<DriveRoundDto> getAllRounds(String driveId);
}
