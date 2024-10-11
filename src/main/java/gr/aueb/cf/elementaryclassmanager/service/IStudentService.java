package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.model.Student;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.StudentAlreadyExists;

import java.util.List;

public interface IStudentService {

    Student insertStudent(StudentInsertDTO student) throws StudentAlreadyExists;
    Student updateStudent(StudentUpdateDTO student) throws EntityNotFoundException;
    void deleteStudent(Long id) throws EntityNotFoundException;
    List<StudentReadOnlyDTO> getStudentByLastname(String lastName) throws EntityNotFoundException;
    StudentReadOnlyDTO getStudentById(Long id) throws EntityNotFoundException;
    StudentReadOnlyDTO getStudentBySsn(String ssn) throws EntityNotFoundException;



}
