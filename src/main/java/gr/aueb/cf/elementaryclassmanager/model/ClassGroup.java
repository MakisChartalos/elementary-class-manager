package gr.aueb.cf.elementaryclassmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "class_group")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClassGroup extends IdentifiableEntity {

    @Getter
    private static final int maxStudents = 20;

    @Column(name = "name", nullable = false)
    private Character name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Grade grade;

    @Column(name = "archived", nullable = false)
    private boolean archived = false;

    @OneToMany(mappedBy = "classGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Student> students = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade =  {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    public ClassGroup(Character name, Grade grade) {
        this.name = name;
        this.grade = grade;
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setClassGroup(this);
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setClassGroup(null);
    }

    public void addTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.setClassgroup(this);
    }

    public void removeTeacher(Teacher teacher) {
        this.teacher = teacher;
        teacher.setClassgroup(null);
    }



}
