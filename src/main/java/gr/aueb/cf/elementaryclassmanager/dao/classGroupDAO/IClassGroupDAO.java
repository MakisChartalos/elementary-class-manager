package gr.aueb.cf.elementaryclassmanager.dao.classGroupDAO;

import gr.aueb.cf.elementaryclassmanager.model.ClassGroup;
import gr.aueb.cf.elementaryclassmanager.model.Grade;

import java.util.List;
import java.util.Optional;

public interface IClassGroupDAO {
    ClassGroup insertClassGroup(ClassGroup classGroup);
    ClassGroup updateClassGroup(ClassGroup classGroup);
    void deleteClassGroup(Long id);
    Optional<ClassGroup> getById(Long id);
    Optional<ClassGroup> getByNameAndGrade(Character name, Grade grade);

}
