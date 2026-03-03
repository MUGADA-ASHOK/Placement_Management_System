package org.example.placement_drive_management.service.Impl;

import org.example.placement_drive_management.dto.ApplicationsDto;
import org.example.placement_drive_management.dto.DriveRoundDto;
import org.example.placement_drive_management.dto.StudentProfileDto;
import org.example.placement_drive_management.entity.Applications;
import org.example.placement_drive_management.entity.DriveRound;
import org.example.placement_drive_management.entity.Student;
import org.example.placement_drive_management.entity.StudentProfile;
import org.example.placement_drive_management.exceptions.ResourceNotFoundException;
import org.example.placement_drive_management.mappers.ApplicationsMapper;
import org.example.placement_drive_management.mappers.DriveRoundMapper;
import org.example.placement_drive_management.repository.ApplicationRepository;
import org.example.placement_drive_management.repository.StudentProfileRepository;
import org.example.placement_drive_management.repository.StudentRepository;
import org.example.placement_drive_management.service.StudentProfileService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class StudentProfileServiceImpl implements StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentRepository studentRepository;
    private final ApplicationRepository applicationRepository;
    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository, StudentRepository studentRepository,ApplicationRepository applicationRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.studentRepository = studentRepository;
        this.applicationRepository=applicationRepository;
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
                studentProfile.setGender(studentProfileDto.getGender());
                studentProfile.setPassingYear(studentProfileDto.getPassingYear());
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
        existingProfile.setGender(studentProfileDto.getGender());
        existingProfile.setPassingYear(studentProfileDto.getPassingYear());
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
                studentProfileDto.getHasbackloghistory(),
                studentProfileDto.getPassingYear(),
                studentProfileDto.getGender()
        );
    }
    @Override
    public List<ApplicationsDto> getAllApplicationsForStudent(String studentRollNo) {
        StudentProfile studentProfile= studentProfileRepository.findByStudentRollNo(studentRollNo).orElseThrow(()->new ResourceNotFoundException("Student with Roll No :"+studentRollNo+"not found"));
        return studentProfile.getApplicationsList().stream().map(ApplicationsMapper::mapToApplicationDto).collect(Collectors.toList());
    }

    @Override
    public List<DriveRoundDto> getAllDriveRoundsForStudent(String studentRollNo, String driveId) {
        Applications application = applicationRepository.findByStudent_RollNo(studentRollNo).orElseThrow(()-> new ResourceNotFoundException("Application with Roll No :"+studentRollNo+"not found"));
        return application.getDriveRounds().stream().map(DriveRoundMapper::mapToDriveRoundDto).collect(Collectors.toList());
    }
}
