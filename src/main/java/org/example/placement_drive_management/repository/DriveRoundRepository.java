package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.entity.DriveRound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRoundRepository extends JpaRepository<DriveRound, Long> {

}
