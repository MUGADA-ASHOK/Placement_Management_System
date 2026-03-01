package org.example.placement_drive_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length=256)
    private String companyCode;
    private String companyName;
    private Double minimumCgpa;
    private Boolean hasHistoryBacklog;
    private String description;
    private String rollsOffered;
    private String skills;
    @OneToMany(mappedBy = "company")
    private List<Applications> applications;

}
