package gr.aueb.cf.elementaryclassmanager.dao.teacherDAO;

import gr.aueb.cf.elementaryclassmanager.model.Teacher;

import java.util.List;
import java.util.Optional;

public interface ITeacherDAO {

    Teacher insertTeacher(Teacher teacher);
    Teacher updateTeacher(Teacher teacher);
    void deleteTeacher(Long id);
    List<Teacher> getByLastname(String lastname);
    Optional<Teacher> getById(Long id);
    Optional<Teacher> getByRegistrationNumber(String registrationNumber);
}


