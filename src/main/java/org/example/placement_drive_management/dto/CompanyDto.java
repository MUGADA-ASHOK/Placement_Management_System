package org.example.placement_drive_management.dto;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import org.example.placement_drive_management.entity.Applications;

import java.util.List;

public class CompanyDto {
    private Long id;
    private String companyName;
    private String description;
    private String rolesOffered;
    private Double packageOffered;
    private Double minimumCgpa;
    private List<Applications> applications;
}
