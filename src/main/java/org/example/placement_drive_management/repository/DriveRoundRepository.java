package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.entity.Applications;
import org.example.placement_drive_management.entity.DriveRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriveRoundRepository extends JpaRepository<DriveRound, Long> {
    Optional<List<DriveRound>> findByDrive_DriveId(String driveId);
}
