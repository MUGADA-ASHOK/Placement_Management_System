package org.example.placement_drive_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Applications {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(
            regexp = "^(ELIGIBLE|SHORTLISTED|APPLIED|REJECTED|INPROCESS|SELECTED)$",
            message = "Application status must be SHORTLISTED, APPLIED, REJECTED, INPROCESS, or SELECTED"
    )
    private String status;

    private Integer currentRoundNumber;

    private Boolean externalApplied;

    private LocalDate appliedDate;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "rollNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "drive_id", referencedColumnName = "driveId")
    private Drive drive;

    @ManyToOne
    @JoinColumn(name = "student_profile")
    private StudentProfile studentProfile;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ApplicationRound> applicationRounds;
}