package org.example.placement_drive_management.mappers;

import org.example.placement_drive_management.dto.StudentDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Student;

public class StudentMapper {
    public static Student maptoStudent(StudentDto studentDto){
        return new Student(
                studentDto.getId(),
                studentDto.getRollNo(),
                studentDto.getName(),
                studentDto.getSurname(),
                studentDto.getEmail(),
                studentDto.getPassword(),
                null

        );
    }
    public static StudentDto maptoStudentDto(Student student){
        return new  StudentDto(
                student.getId(),
                student.getRollNo(),
                student.getName(),
                student.getSurname(),
                student.getEmail(),
                student.getPassword()
        );
    }
}
