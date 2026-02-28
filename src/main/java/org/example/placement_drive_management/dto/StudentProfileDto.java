package org.example.placement_drive_management.dto;

import lombok.AllArgsConstructor;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentProfileDto {
        private Long id;
        private String department;
        private Double tenthPercentage;
        private Double twelthPercentage;
        private Double diplomaPercentage;
        private Double currentCgpa;
        private Integer currentSemester;
        private Integer backlogCount;
        private Boolean hasBacklogHistory;

}
