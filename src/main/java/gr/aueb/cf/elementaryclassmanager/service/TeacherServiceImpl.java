package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dao.teacherDAO.ITeacherDAO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.mapper.Mapper;
import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.TeacherAlreadyExists;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Provider
@ApplicationScoped
public class TeacherServiceImpl implements ITeacherService {

    @Inject
    private ITeacherDAO teacherDAO;

    /**
     * Inserts a new teacher into the system.
     *
     * @param dto the data transfer object containing teacher details
     * @return the inserted teacher
     * @throws TeacherAlreadyExists if a teacher with the same registration number already exists
     */
    @Override
    public TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO dto) throws TeacherAlreadyExists {
        Teacher teacherToInsert;

        try {
            JPAHelper.beginTransaction();
            teacherDAO.getByRegistrationNumber(dto.getRegistrationNumber())
                    .ifPresent(existingTeacher -> {
                        throw new TeacherAlreadyExists(existingTeacher.getRegistrationNumber());
                    });

            teacherToInsert = Mapper.mapToTeacher(dto);
            teacherDAO.insertTeacher(teacherToInsert);
            JPAHelper.commitTransaction();
            log.info("Teacher with id " + teacherToInsert.getId() + " was inserted");
        } catch (TeacherAlreadyExists e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
        return Mapper.mapToTeacherReadOnlyDTO(teacherToInsert);
    }

    /**
     * Updates an existing teacher in the system.
     *
     * @param dto the data transfer object containing updated teacher details
     * @return the updated teacher
     * @throws EntityNotFoundException if the teacher with the given ID does not exist
     */
    @Override
    public TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException {
        Teacher teacherToUpdate;
        Teacher updatedTeacher;

        try {
            JPAHelper.beginTransaction();
            teacherToUpdate = teacherDAO.getById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, dto.getId()));

            updatedTeacher = Mapper.mapToTeacher(teacherToUpdate, dto);
            teacherDAO.updateTeacher(updatedTeacher);
            JPAHelper.commitTransaction();
            log.info("Teacher with id " + teacherToUpdate.getId() + " was updated");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
        return Mapper.mapToTeacherReadOnlyDTO(updatedTeacher);
    }

    /**
     * Deletes a teacher from the system.
     *
     * @param id the ID of the teacher to be deleted
     * @throws EntityNotFoundException if the teacher with the given ID does not exist
     */
    @Override
    public void deleteTeacher(Long id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Teacher teacherToDelete = teacherDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, id));

            teacherDAO.deleteTeacher(id);
            JPAHelper.commitTransaction();
            log.info("Teacher with id " + teacherToDelete.getId() + " was deleted");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }

    /**
     * Retrieves teachers by their last name.
     *
     * @param lastname the last name of the teachers to search for
     * @return a list of read-only DTOs of the teachers found
     * @throws EntityNotFoundException if no teachers are found with the given last name
     */
    @Override
    public List<TeacherReadOnlyDTO> getTeachersByLastname(String lastname) throws EntityNotFoundException {
        List<Teacher> teachers;

        try {
            JPAHelper.beginTransaction();
            teachers = teacherDAO.getByLastname(lastname);
            if (teachers.isEmpty()) {
                throw new EntityNotFoundException(Teacher.class, "lastname", lastname);
            }
            JPAHelper.commitTransaction();
            log.info("Teachers with lastname like " + lastname + " were found");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
        return teachers.stream()
                .map(Mapper::mapToTeacherReadOnlyDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a teacher by their ID.
     *
     * @param id the ID of the teacher to retrieve
     * @return the read-only DTO of the teacher found
     * @throws EntityNotFoundException if the teacher with the given ID does not exist
     */
    @Override
    public TeacherReadOnlyDTO getTeacherById(Long id) throws EntityNotFoundException {
        Teacher teacherToReturn;

        try {
            JPAHelper.beginTransaction();
            teacherToReturn = teacherDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, id));
            JPAHelper.commitTransaction();
            log.info("Teacher with id " + teacherToReturn.getId() + " was found");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return Mapper.mapToTeacherReadOnlyDTO(teacherToReturn);
    }
}
