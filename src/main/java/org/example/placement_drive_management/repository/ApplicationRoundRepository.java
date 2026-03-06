package org.example.placement_drive_management.repository;

import org.example.placement_drive_management.entity.ApplicationRound;
import org.example.placement_drive_management.entity.Applications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRoundRepository extends JpaRepository<ApplicationRound, Long> {


    @Query("""
        SELECT ar.application
        FROM applicationRound ar
        WHERE ar.driveRound.drive.driveId = :driveId
        AND ar.driveRound.roundNumber = :roundNumber
        AND ar.status = :status
    """)

     List<Applications> findClearedApplicationsFromPreviousRound(
            String driveId,
            Integer roundNumber,
            String status
    );



    @Query("""
    SELECT ar
    FROM applicationRound ar
    JOIN FETCH ar.application a
    JOIN FETCH a.student
    JOIN FETCH ar.driveRound dr
    WHERE dr.drive.driveId = :driveId
    AND dr.roundNumber = :roundNumber
""")
    List<ApplicationRound> findDetailedRoundApplicants(
            @Param("driveId") String driveId,
            @Param("roundNumber") Integer roundNumber
    );


}
