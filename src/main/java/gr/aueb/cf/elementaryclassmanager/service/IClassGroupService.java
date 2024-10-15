package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.ClassGroupAlreadyExists;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.ClassGroupFullException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.StudentGradeDoesNotMatchClassGroup;

import java.util.List;

public interface IClassGroupService {
    ClassGroupReadOnlyDTO insertClassGroup(ClassGroupInsertDTO dto) throws ClassGroupAlreadyExists;
    ClassGroupReadOnlyDTO updateClassGroup(ClassGroupUpdateDTO dto) throws EntityNotFoundException;
    void deleteClassGroup(Long id) throws EntityNotFoundException;
    List<StudentReadOnlyDTO> getStudentsByClassGroupId(Long classGroupId) throws EntityNotFoundException;
    void assignStudentToClassGroup(Long studentId, Long classGroupId) throws EntityNotFoundException, ClassGroupFullException, StudentGradeDoesNotMatchClassGroup;
    void assignTeacherToClassGroup(Long teacherId, Long classGroupId) throws EntityNotFoundException;
    void incrementGradeForClassGroup(Long classGroupId) throws EntityNotFoundException;

}