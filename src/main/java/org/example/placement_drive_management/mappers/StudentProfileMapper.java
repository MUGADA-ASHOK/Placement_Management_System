package org.example.placement_drive_management.mappers;

import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.StudentProfile;

public class StudentProfileMapper {
    public static StudentProfileDto maptoStudentProfileDto(StudentProfile studentProfileDto){
        StudentProfileDto studentProfile = new StudentProfileDto();
        studentProfile.setId(studentProfileDto.getId());
        studentProfile.setDepartment(studentProfileDto.getDepartment());
        studentProfile.setCurrentSemester(studentProfileDto.getCurrentSemester());
        studentProfile.setHasBacklogHistory(studentProfileDto.getHasbackloghistory());
        studentProfile.setBacklogCount(studentProfileDto.getBacklogCount());
        studentProfile.setCurrentCgpa(studentProfileDto.getCurrentCgpa());
        studentProfile.setTenthPercentage(studentProfileDto.getTenthPercentage());
        studentProfile.setDiplomaPercentage(studentProfileDto.getDiplomaPercentage());
        studentProfile.setTwelthPercentage(studentProfileDto.getTwelthPercentage());
        studentProfile.setPassingYear(studentProfileDto.getPassingYear());
        studentProfile.setGender(studentProfileDto.getGender());
        return studentProfile;
    }
    public static StudentProfile maptoStudentProfileDto(StudentProfileDto studentProfileDto){
        StudentProfile studentProfile = new StudentProfile();
        studentProfile.setId(studentProfileDto.getId());
        studentProfile.setDepartment(studentProfileDto.getDepartment());
        studentProfile.setCurrentSemester(studentProfileDto.getCurrentSemester());
        studentProfile.setHasbackloghistory(studentProfileDto.getHasBacklogHistory());
        studentProfile.setBacklogCount(studentProfileDto.getBacklogCount());
        studentProfile.setCurrentCgpa(studentProfileDto.getCurrentCgpa());
        studentProfile.setTenthPercentage(studentProfileDto.getTenthPercentage());
        studentProfile.setDiplomaPercentage(studentProfileDto.getDiplomaPercentage());
        studentProfile.setTwelthPercentage(studentProfileDto.getTwelthPercentage());
        studentProfile.setPassingYear(studentProfileDto.getPassingYear());
        studentProfile.setGender(studentProfileDto.getGender());
        return studentProfile;
    }


}
