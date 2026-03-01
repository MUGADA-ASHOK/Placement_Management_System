package org.example.placement_drive_management.service.Impl;

import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.entity.StudentProfile;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.repository.StudentProfileRepository;
import org.example.placement_drive_management.repository.StudentRepository;
import org.example.placement_drive_management.service.StudentProfileService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service

public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentRepository studentRepository;
    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository, StudentRepository studentRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public String createStudentProfile(String rollNo, StudentProfileDto studentProfileDto) {

        Student student = studentRepository.findByRollNo(rollNo)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student with RollNo "
                                + rollNo + " not found"));

        StudentProfile studentProfile = new StudentProfile();
                studentProfile.setStudent((student));
                studentProfile.setDepartment(studentProfileDto.getDepartment());
                studentProfile.setCurrentSemester(studentProfileDto.getCurrentSemester());
                studentProfile.setHasbackloghistory(studentProfileDto.getHasBacklogHistory());
                studentProfile.setBacklogCount(studentProfileDto.getBacklogCount());
                studentProfile.setCurrentCgpa(studentProfileDto.getCurrentCgpa());
                studentProfile.setTenthPercentage(studentProfileDto.getTenthPercentage());
                studentProfile.setDiplomaPercentage(studentProfileDto.getDiplomaPercentage());
                studentProfile.setTwelthPercentage(studentProfileDto.getTwelthPercentage());
        studentProfileRepository.save(studentProfile);

        return "Profile Created Successfully";
    }

    @Override
    public String updateStudentProfile(String rollNo,
                                       StudentProfileDto studentProfileDto) {

        StudentProfile existingProfile =
                studentProfileRepository.findByStudentRollNo(rollNo)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Student with RollNo "
                                        + rollNo + " not found"));

        existingProfile.setDepartment(studentProfileDto.getDepartment());
        existingProfile.setTenthPercentage(studentProfileDto.getTenthPercentage());
        existingProfile.setTwelthPercentage(studentProfileDto.getTwelthPercentage());
        existingProfile.setDiplomaPercentage(studentProfileDto.getDiplomaPercentage());
        existingProfile.setCurrentCgpa(studentProfileDto.getCurrentCgpa());
        existingProfile.setCurrentSemester(studentProfileDto.getCurrentSemester());
        existingProfile.setBacklogCount(studentProfileDto.getBacklogCount());
        existingProfile.setHasbackloghistory(studentProfileDto.getHasBacklogHistory());

        studentProfileRepository.save(existingProfile);

        return "Profile Updated Successfully";
    }
    @Override
    public StudentProfileDto getStudentProfile(String rollNo) {
        StudentProfile studentProfileDto=studentProfileRepository.findByStudentRollNo(rollNo)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student with RollNo "
                                + rollNo + " not found"));
        return new StudentProfileDto(
                studentProfileDto.getId(),
                studentProfileDto.getDepartment(),
                studentProfileDto.getTenthPercentage(),
                studentProfileDto.getTwelthPercentage(),
                studentProfileDto.getDiplomaPercentage(),
                studentProfileDto.getCurrentCgpa(),
                studentProfileDto.getCurrentSemester(),
                studentProfileDto.getBacklogCount(),
                studentProfileDto.getHasbackloghistory()
        );
    }
}
