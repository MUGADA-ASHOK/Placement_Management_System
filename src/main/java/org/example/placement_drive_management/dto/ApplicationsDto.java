package org.example.placement_drive_management.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationsDto {
    private Long id;

    private String studentRollNo;

    private Long driveId;

    private String status;

    private Integer currentRound;

    private LocalDateTime appliedAt;

    private Boolean offerAccepted;
}


