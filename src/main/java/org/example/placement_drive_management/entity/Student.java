package org.example.placement_drive_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="student")
public class Student {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    @Size(min = 12, max = 12)
    private String rollNo;
    @NotNull
    @Column(length=15)
    private String name;
    @NotNull
    @Column(length=15)
    private String surname;
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@anits\\.edu\\.in$",message = "Enter the valid college email")
    @Column(length=100)
    private String email;
    @NotNull
    @Column(length=256)
    private String password;
    @OneToOne(mappedBy = "student")
    private StudentProfile studentProfile;
    private String role="user";
}
