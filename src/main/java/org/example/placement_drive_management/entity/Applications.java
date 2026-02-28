package org.example.placement_drive_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_roll_no",referencedColumnName = "rollNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Pattern(regexp = "^(APPLIED| SHORTLISTED| APTITUDE_CLEARED|TECHNICAL_CLEARED|HR_CLEARED|SELECTED|REJECTED)")
    private String status;

    private Integer currentRound;

    private LocalDateTime appliedAt;

    private Boolean offerAccepted;
}
