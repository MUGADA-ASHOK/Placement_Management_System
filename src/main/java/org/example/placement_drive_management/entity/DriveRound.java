package org.example.placement_drive_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriveRound {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer roundNumber;

    private String roundName;

    private LocalDate roundDate;

    private String roundLink;
    @ManyToOne
    @JoinColumn(name = "drive_id",referencedColumnName = "driveId")
    private Drive drive;
}