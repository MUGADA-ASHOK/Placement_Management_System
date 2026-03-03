package org.example.placement_drive_management.controllers;

import lombok.AllArgsConstructor;
import org.example.placement_drive_management.dto.DriveDto;
import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/company")
@AllArgsConstructor
public class CompanyController {
    private CompanyService companyService;
    @PostMapping("/publishDriveRound/{driveId}")
    public ResponseEntity<String> publishDriveRound(@PathVariable String driveId, DriveRoundDto driveRoundDto) {
        return  ResponseEntity.ok(companyService.publishDriveRound(driveId,driveRoundDto));
    }

    @GetMapping("/getAllDrives")
    public ResponseEntity<List<DriveDto>> getAllDrives(){
        return ResponseEntity.ok(companyService.getAllDrives());
    }

    @GetMapping("/getRounds/{driveId}")
    public ResponseEntity<List<DriveRoundDto>> getRounds(@PathVariable String driveId){
        return ResponseEntity.ok(companyService.getAllRounds(driveId));
    }

}
