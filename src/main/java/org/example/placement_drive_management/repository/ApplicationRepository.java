package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Applications, Long> {
}
