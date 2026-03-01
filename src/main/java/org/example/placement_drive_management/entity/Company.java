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
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String companyId;
    private String companyName;

    private String website;

    private String industryType;

    private String description;

    private String externalApplicationLink;
    @OneToMany(mappedBy = "company")
    List<Drive> drives;
}
