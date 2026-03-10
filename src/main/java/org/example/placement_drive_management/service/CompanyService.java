package org.example.placement_drive_management.service;

import org.example.placement_drive_management.dto.ApplicationRoundDto;
import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;

import java.util.List;

public interface CompanyService {

    String publishDriveRound(String driveId, DriveRoundDto driveRoundDto, String companyId);

    List<DriveDto> getAllDrives(String companyId);

    List<DriveRoundDto> getAllRounds(String driveId, String companyId);  // ← added companyId

    List<ApplicationsDto> getAllApplications(String driveId, String companyId);

    List<ApplicationRoundDto> getApplicantsForDriveRound(String driveId, Integer roundNo, String companyId);  // ← added companyId

    String publishScoreForDriveRound(String driveId, String rollNo, Integer roundNo, Double score, String companyId);  // ← added companyId

    String filterTopKStudents(String driveId, Integer roundNo, Integer topK, String companyId);  // ← added companyId

    String filterByCutOffMarks(String driveId, Integer roundNo, Double cutOffMarks, String companyId);  // ← added companyId
}