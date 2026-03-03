package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.*;
@Repository
public interface ApplicationRepository extends JpaRepository<Applications, Long> {
    Optional<List<Applications>> findByDrive_DriveId(String driveId);
    Optional<Applications>findByStudent_RollNo(String rollNo);

}
