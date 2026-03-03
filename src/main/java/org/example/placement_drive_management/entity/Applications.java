package org.example.placement_drive_management.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.example.placement_drive_management.dto.DriveRoundDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Pattern(regexp = "^(ELIGIBLE|SHORTLISTED|APPLIED|REJECTED|INPROCESS|SELECTED)$",message = "Application status must be SHORTLISTED APPLIED, REJECTED, INPROCESS, or SELECTED" )
    private String status;
    private Integer currentRoundNumber;

    private Boolean externalApplied;

    private LocalDate appliedDate;

    @ManyToOne
    @JoinColumn(name = "student_id",referencedColumnName = "rollNo")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "drive_id",referencedColumnName = "driveId")
    private Drive drive;
    @ManyToOne
    @JoinColumn(name = "student_profile")
    private StudentProfile studentProfile;
    @OneToMany(mappedBy = "applications", cascade = CascadeType.ALL)
    private List<DriveRound> driveRounds=new ArrayList<>();
}
