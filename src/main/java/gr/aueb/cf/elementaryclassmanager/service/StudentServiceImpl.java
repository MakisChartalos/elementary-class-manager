package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dao.studentDAO.IStudentDAO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.mapper.Mapper;
import gr.aueb.cf.elementaryclassmanager.model.Student;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.StudentAlreadyExists;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Provider
@ApplicationScoped
@Slf4j
public class StudentServiceImpl implements IStudentService {

    @Inject
    IStudentDAO studentDAO;

    /**
     * Inserts a new student into the system.
     *
     * @param dto the data transfer object containing student details
     * @return the inserted student
     * @throws StudentAlreadyExists if a student with the same SSN already exists
     */
    @Override
    public Student insertStudent(StudentInsertDTO dto) throws StudentAlreadyExists {
        Student studentToInsert;

        try {
            JPAHelper.beginTransaction();
            studentDAO.getBySsn(dto.getSsn())
                    .ifPresent(existingStudent -> {
                        throw new StudentAlreadyExists(dto.getSsn());
                    });

            studentToInsert = studentDAO.insertStudent(Mapper.mapToStudent(dto));
            JPAHelper.commitTransaction();
            log.info("Student with SSN " + dto.getSsn() + " inserted successfully");
        } catch (StudentAlreadyExists e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
        return studentToInsert;
    }

    /**
     * Updates an existing student's information based on the provided data transfer object.
     * If the student's grade changes, the student is removed from their current ClassGroup
     * to ensure consistency. The grade change is then applied to the student.
     *
     * @param dto the data transfer object containing the updated student details.
     * @return the updated Student entity.
     * @throws EntityNotFoundException if the student with the specified ID does not exist.
     */
    @Override
    public Student updateStudent(StudentUpdateDTO dto) throws EntityNotFoundException {
        Student studentToUpdate;
        Student updatedStudent;

        try {
            JPAHelper.beginTransaction();
            studentToUpdate = studentDAO.getActiveById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Student.class, dto.getId()));

            // Check if the grade has changed and remove the student from the current ClassGroup if necessary
            if (!studentToUpdate.getGrade().equals(dto.getGrade())) {
                if (studentToUpdate.getClassGroup() != null) {
                    studentToUpdate.getClassGroup().removeStudent(studentToUpdate);
                }
                log.info("Student with id " + dto.getId() + " was removed from their previous ClassGroup due to grade change.");
            }

            // Map the updates and save
            updatedStudent = Mapper.mapToStudent(studentToUpdate, dto);
            studentDAO.updateStudent(updatedStudent);
            JPAHelper.commitTransaction();
            log.info("Student with id " + dto.getId() + " updated successfully");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return updatedStudent;
    }

    /**
     * Deletes a student from the system.
     *
     * @param id the ID of the student to be deleted
     * @throws EntityNotFoundException if the student with the given ID does not exist
     */
    @Override
    public void deleteStudent(Long id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Student studentToDelete = studentDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Student.class, id));

            studentDAO.deleteStudent(id);
            JPAHelper.commitTransaction();
            log.info("Student with id " + id + " deleted successfully");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }

    /**
     * Retrieves students by their last name.
     *
     * @param lastName the last name of the students to search for
     * @return a list of read-only DTOs of the students found
     * @throws EntityNotFoundException if no students are found with the given last name
     */
    @Override
    public List<StudentReadOnlyDTO> getStudentByLastname(String lastName) throws EntityNotFoundException {
        List<Student> students;

        try {
            JPAHelper.beginTransaction();
            students = studentDAO.getByLastName(lastName);
            if (students.isEmpty()) {
                throw new EntityNotFoundException(Student.class, "lastname", lastName);
            }
            JPAHelper.commitTransaction();
            log.info("Students with lastname " + lastName + " found successfully");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
        return students.stream()
                .map(Mapper::mapToStudentReadOnlyDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id the ID of the student to retrieve
     * @return the read-only DTO of the student found
     * @throws EntityNotFoundException if the student with the given ID does not exist
     */
    @Override
    public StudentReadOnlyDTO getStudentById(Long id) throws EntityNotFoundException {
        Student studentToReturn;

        try {
            JPAHelper.beginTransaction();
            studentToReturn = studentDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Student.class, id));
            JPAHelper.commitTransaction();
            log.info("Student with id " + studentToReturn.getId() + " found successfully");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return Mapper.mapToStudentReadOnlyDTO(studentToReturn);
    }

    /**
     * Retrieves a student by their SSN.
     *
     * @param ssn the SSN of the student to retrieve
     * @return the read-only DTO of the student found
     * @throws EntityNotFoundException if the student with the given SSN does not exist
     */
    @Override
    public StudentReadOnlyDTO getStudentBySsn(String ssn) throws EntityNotFoundException {
        Student studentToReturn;

        try {
            JPAHelper.beginTransaction();
            studentToReturn = studentDAO.getBySsn(ssn)
                    .orElseThrow(() -> new EntityNotFoundException(Student.class, "SSN", ssn));
            JPAHelper.commitTransaction();
            log.info("Student with SSN " + ssn + " found successfully");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return Mapper.mapToStudentReadOnlyDTO(studentToReturn);
    }
}
