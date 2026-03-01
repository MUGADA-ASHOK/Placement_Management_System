package org.example.placement_drive_management.mappers;

import org.example.placement_drive_management.dto.AdminDto;
import org.example.placement_drive_management.entity.Admin;

public class AdminMapper {
    public static AdminDto maptoAdminDto(Admin admin) {
        return new  AdminDto(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getPassword()
        );
    }

    public static Admin maptoAdmin(AdminDto adminDto) {
        return new  Admin(
                adminDto.getId(),
                adminDto.getUsername(),
                adminDto.getEmail(),
                adminDto.getPassword()
        );
    }
}
