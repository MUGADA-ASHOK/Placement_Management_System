package org.example.placement_drive_management.mappers;

import org.example.placement_drive_management.dto.StudentResponseDto;
import org.example.placement_drive_management.entity.Student;

public class StudentResponseMapper {
    public static StudentResponseDto maptoStudentResponseDto(Student student) {
        return new StudentResponseDto(
                student.getRollNo(),
                student.getName(),
                student.getSurname()
        );
    }
}
