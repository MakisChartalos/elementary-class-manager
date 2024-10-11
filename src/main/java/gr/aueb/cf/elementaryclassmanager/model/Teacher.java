package gr.aueb.cf.elementaryclassmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "teachers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Teacher extends IdentifiableEntity {


    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(name = "email", unique = true)
    private String email;

    @OneToOne(mappedBy = "teacher")
    private ClassGroup classgroup;

    public Teacher(String firstname, String lastname, String teacherRegistrationNumber, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.registrationNumber = teacherRegistrationNumber;
        this.email = email;
    }
}