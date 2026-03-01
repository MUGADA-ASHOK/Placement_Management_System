package org.example.placement_drive_management.mappers;

import org.example.placement_drive_management.dto.CompanyDto;
import org.example.placement_drive_management.entity.Company;

import java.util.ArrayList;

public class CompanyMapper {
    public static CompanyDto mapToCompanyDto(Company company) {
        return new  CompanyDto(
                company.getId(),
                company.getCompanyId(),
                company.getCompanyName(),
                company.getWebsite(),
                company.getIndustryType(),
                company.getDescription(),
                company.getExternalApplicationLink()
        );
    }
    public static Company mapToCompany(CompanyDto companyDto) {
        return new  Company(
                companyDto.getId(),
                companyDto.getCompanyId(),
                companyDto.getCompanyName(),
                companyDto.getWebsite(),
                companyDto.getIndustryType(),
                companyDto.getDescription(),
                companyDto.getExternalApplicationLink(),
                new ArrayList<>()
        );
    }
}
