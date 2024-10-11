package gr.aueb.cf.elementaryclassmanager.dao.studentDAO;

import gr.aueb.cf.elementaryclassmanager.model.Student;

import java.util.List;
import java.util.Optional;

public interface IStudentDAO {
    Student insertStudent(Student student);
    Student updateStudent(Student student);
    void deleteStudent(Long id);
    List<Student> getByLastName(String lastName);
    Optional<Student> getById(Long id);
    Optional<Student> getBySsn(String ssn);
    List<Student> getByClassGroupId(Long classGroupId);
    void archiveStudent(Long studentId);
    Optional<Student> getActiveById(Long id);
}
