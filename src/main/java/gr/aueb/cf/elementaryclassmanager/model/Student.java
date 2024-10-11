package gr.aueb.cf.elementaryclassmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student extends IdentifiableEntity{

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(name = "archived", nullable = false)
    private boolean archived = false;

    @ManyToOne
    @JoinColumn(name = "class_group_id", referencedColumnName = "id")
    private ClassGroup classGroup;

    public Student(String firstname, String lastname, String ssn, Grade grade) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.ssn = ssn;
        this.grade = grade;
    }

    public void addClassGroup(ClassGroup classGroup) {
        this.classGroup = classGroup;
        classGroup.getStudents().add(this);
    }
}
