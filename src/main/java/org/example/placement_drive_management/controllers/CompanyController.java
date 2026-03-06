package org.example.placement_drive_management.controllers;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.ApplicationRoundDto;
import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company")   // ← FIXED: was /api/auth/company
@AllArgsConstructor
public class CompanyController {

    private CompanyService companyService;

    @PostMapping("/publishDriveRound/{driveId}")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<String> publishDriveRound(
            @PathVariable String driveId,
            @RequestBody DriveRoundDto driveRoundDto) {
        return ResponseEntity.ok(companyService.publishDriveRound(driveId, driveRoundDto));
    }

    @GetMapping("/getAllDrives")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<List<DriveDto>> getAllDrives() {
        return ResponseEntity.ok(companyService.getAllDrives());
    }

    @GetMapping("/getRounds/{driveId}")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<List<DriveRoundDto>> getRounds(@PathVariable String driveId) {
        return ResponseEntity.ok(companyService.getAllRounds(driveId));
    }

    @GetMapping("/allApplications/{driveId}")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<List<ApplicationsDto>> getAllApplications(@PathVariable String driveId) {
        return ResponseEntity.ok(companyService.getAllApplications(driveId));
    }

    @GetMapping("/allApplications/{driveId}/{roundNumber}")
    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public ResponseEntity<List<ApplicationRoundDto>> getAllApplicationsByRound(
            @PathVariable String driveId,
            @PathVariable Integer roundNumber) {
        return ResponseEntity.ok(companyService.getApplicantsForDriveRound(driveId, roundNumber));
    }
}