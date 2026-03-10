package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.entity.Company;
import org.example.placement_drive_management.entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive,Long> {
    Optional<Drive> findByDriveId(String driveId);
    Boolean existsByDriveId(String driveId);
    List<Drive> findByCompany_CompanyId(String companyId);
}
