package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Applications, Long> {

    Optional<List<Applications>> findByDrive_DriveId(String driveId);

    Optional<Applications> findByStudent_RollNo(String rollNo);
    Optional<Applications> findByDrive_DriveIdAndStudent_RollNo(
            String driveId,
            String rollNo
    );
}