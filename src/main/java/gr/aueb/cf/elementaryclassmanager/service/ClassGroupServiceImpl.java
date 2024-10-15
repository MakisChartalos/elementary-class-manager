package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dao.classGroupDAO.IClassGroupDAO;
import gr.aueb.cf.elementaryclassmanager.dao.studentDAO.IStudentDAO;
import gr.aueb.cf.elementaryclassmanager.dao.teacherDAO.ITeacherDAO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.mapper.Mapper;
import gr.aueb.cf.elementaryclassmanager.model.ClassGroup;
import gr.aueb.cf.elementaryclassmanager.model.Grade;
import gr.aueb.cf.elementaryclassmanager.model.Student;
import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.*;
import gr.aueb.cf.elementaryclassmanager.service.util.JPAHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing ClassGroups.
 * Provides methods to insert, update, delete, and assign students and teachers to ClassGroups.
 * Also includes utility methods for checking if a ClassGroup is full.
 */
@Provider
@ApplicationScoped
@Slf4j
public class ClassGroupServiceImpl implements IClassGroupService {

    @Inject
    private IClassGroupDAO classGroupDAO;

    @Inject
    private IStudentDAO studentDAO;

    @Inject
    private ITeacherDAO teacherDAO;

    /**
     * Checks if a ClassGroup is full.
     *
     * @param classGroup the ClassGroup to check.
     * @return {@code true} if the number of students in the ClassGroup is equal to or greater than the maximum allowed, {@code false} otherwise.
     */
    private boolean isClassGroupFull(ClassGroup classGroup) {
        return classGroup.getStudents().size() >= ClassGroup.getMaxStudents();
    }

    /**
     * Checks if the student's grade matches the class group's grade.
     *
     * @param student    the student to check.
     * @param classGroup the class group to check against.
     * @return true if the grades match, false otherwise.
     */
    private boolean isStudentGradeMatchingClassGroup(Student student, ClassGroup classGroup) {
        return student.getGrade().equals(classGroup.getGrade());
    }

    /**
     * Inserts a new ClassGroup.
     *
     * @param dto the data transfer object containing the ClassGroup details to insert.
     * @return the inserted ClassGroup.
     * @throws ClassGroupAlreadyExists if a ClassGroup with the same name and grade already exists.
     */
    @Override
    public ClassGroupReadOnlyDTO insertClassGroup(ClassGroupInsertDTO dto) throws ClassGroupAlreadyExists {
        ClassGroup classGroupToInsert;

        try {
            JPAHelper.beginTransaction();
            classGroupDAO.getByNameAndGrade(dto.getName(), dto.getGrade())
                    .ifPresent(existingClassGroup -> {
                        throw new ClassGroupAlreadyExists(existingClassGroup.getGrade(), existingClassGroup.getName());
                    });

            classGroupToInsert = Mapper.mapToClassGroup(dto);
            classGroupDAO.insertClassGroup(classGroupToInsert);
            JPAHelper.commitTransaction();
            log.info("ClassGroup with name " + classGroupToInsert.getName() + " and grade " + classGroupToInsert.getGrade() + " was inserted successfully.");
        } catch (ClassGroupAlreadyExists e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return Mapper.mapToClassGroupReadOnlyDTO(classGroupToInsert);
    }

    /**
     * Updates an existing ClassGroup.
     *
     * @param dto the data transfer object containing the ClassGroup details to update.
     * @return the updated ClassGroup.
     * @throws EntityNotFoundException if the ClassGroup with the specified ID does not exist.
     */
    @Override
    public ClassGroupReadOnlyDTO updateClassGroup(ClassGroupUpdateDTO dto) throws EntityNotFoundException {
        ClassGroup classGroupToUpdate;
        ClassGroup updatedClassGroup;

        try {
            JPAHelper.beginTransaction();
            classGroupToUpdate = classGroupDAO.getById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException(ClassGroup.class, dto.getId()));

            updatedClassGroup = Mapper.mapToClassGroup(classGroupToUpdate, dto);
            classGroupDAO.updateClassGroup(updatedClassGroup);
            JPAHelper.commitTransaction();
            log.info("ClassGroup with id " + updatedClassGroup.getId() + " was updated successfully.");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }

        return Mapper.mapToClassGroupReadOnlyDTO(updatedClassGroup);
    }

    /**
     * Deletes a ClassGroup by its ID.
     *
     * @param id the ID of the ClassGroup to delete.
     * @throws EntityNotFoundException if the ClassGroup with the specified ID does not exist.
     */
    @Override
    public void deleteClassGroup(Long id) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            ClassGroup classGroupToDelete = classGroupDAO.getById(id)
                    .orElseThrow(() -> new EntityNotFoundException(ClassGroup.class, id));

            classGroupDAO.deleteClassGroup(id);
            JPAHelper.commitTransaction();
            log.info("ClassGroup with id " + classGroupToDelete.getId() + " was deleted successfully.");
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }

    /**
     * Retrieves a list of students belonging to a specific ClassGroup.
     *
     * @param classGroupId the ID of the ClassGroup.
     * @return a list of read-only student DTOs.
     * @throws EntityNotFoundException if no students are found for the given ClassGroup ID.
     */
    @Override
    public List<StudentReadOnlyDTO> getStudentsByClassGroupId(Long classGroupId) throws EntityNotFoundException {
        List<Student> students;

        try {
            JPAHelper.beginTransaction();
            students = studentDAO.getByClassGroupId(classGroupId);
            if (students.isEmpty()) {
                throw new EntityNotFoundException(ClassGroup.class, classGroupId);
            }
            JPAHelper.commitTransaction();
            log.info("Students found for ClassGroup with id " + classGroupId + ".");
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
     * Assigns a student to a ClassGroup.
     *
     * @param studentId    the ID of the student to assign.
     * @param classGroupId the ID of the ClassGroup to assign the student to.
     * @throws EntityNotFoundException if the student or ClassGroup does not exist.
     * @throws ClassGroupFullException if the ClassGroup is already full.
     * @throws StudentGradeDoesNotMatchClassGroup if the student's grade does not match the ClassGroup's grade.
     */
    @Override
    public void assignStudentToClassGroup(Long studentId, Long classGroupId)
            throws EntityNotFoundException, ClassGroupFullException, StudentGradeDoesNotMatchClassGroup {
        try {
            JPAHelper.beginTransaction();
            Student studentToAssign = studentDAO.getActiveById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException(Student.class, studentId));
            ClassGroup classGroupToAssign = classGroupDAO.getById(classGroupId)
                    .orElseThrow(() -> new EntityNotFoundException(ClassGroup.class, classGroupId));

            if (!isStudentGradeMatchingClassGroup(studentToAssign, classGroupToAssign)) {
                throw new StudentGradeDoesNotMatchClassGroup(studentId, classGroupId);
            }
            if (isClassGroupFull(classGroupToAssign)) {
                throw new ClassGroupFullException(classGroupId);
            }

            if (studentToAssign.getClassGroup() != null) {
                studentToAssign.getClassGroup().removeStudent(studentToAssign);
            }

            classGroupToAssign.addStudent(studentToAssign);
            classGroupDAO.updateClassGroup(classGroupToAssign);
            JPAHelper.commitTransaction();
            log.info("Student with id " + studentId + " assigned to ClassGroup with id " + classGroupId);
        } catch (EntityNotFoundException | ClassGroupFullException | StudentGradeDoesNotMatchClassGroup e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }

    /**
     * Assigns a teacher to a ClassGroup.
     *
     * @param teacherId    the ID of the teacher to assign.
     * @param classGroupId the ID of the ClassGroup to assign the teacher to.
     * @throws EntityNotFoundException if the teacher or ClassGroup does not exist.
     */
    @Override
    public void assignTeacherToClassGroup(Long teacherId, Long classGroupId) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();
            Teacher teacherToAssign = teacherDAO.getById(teacherId)
                    .orElseThrow(() -> new EntityNotFoundException(Teacher.class, teacherId));
            ClassGroup classGroupToAssign = classGroupDAO.getById(classGroupId)
                    .orElseThrow(() -> new EntityNotFoundException(ClassGroup.class, classGroupId));

            if (classGroupToAssign.getTeacher() != null) {
                classGroupToAssign.removeTeacher(classGroupToAssign.getTeacher());
            }
            if (teacherToAssign.getClassgroup() != null) {
                teacherToAssign.getClassgroup().removeTeacher(teacherToAssign);
            }

            classGroupToAssign.addTeacher(teacherToAssign);
            classGroupDAO.updateClassGroup(classGroupToAssign);
            JPAHelper.commitTransaction();
            log.info("Teacher with id " + teacherId + " assigned to ClassGroup with id " + classGroupId);
        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }

    /**
     * Increments the grade for a ClassGroup.
     *
     * @param classGroupId the ID of the ClassGroup to increment the grade for.
     * @throws EntityNotFoundException if the ClassGroup with the specified ID does not exist.
     */
    @Override
    public void incrementGradeForClassGroup(Long classGroupId) throws EntityNotFoundException {
        try {
            JPAHelper.beginTransaction();

            ClassGroup classGroupToIncrement = classGroupDAO.getById(classGroupId)
                    .orElseThrow(() -> new EntityNotFoundException(ClassGroup.class, classGroupId));

            Optional<Grade> nextGradeOpt = getNextGradeIfNotFinal(classGroupToIncrement.getGrade());

            if (nextGradeOpt.isEmpty()) {
                archiveStudentsAndDeleteClassGroup(classGroupToIncrement);
                throw new GradeIncrementNotAllowedException();
            }

            Grade nextGrade = nextGradeOpt.get();
            classGroupToIncrement.setGrade(nextGrade);
            classGroupDAO.updateClassGroup(classGroupToIncrement);
            log.info("ClassGroup with ID " + classGroupId + " was incremented to grade " + nextGrade + ".");

            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException | GradeIncrementNotAllowedException e) {
            JPAHelper.rollbackTransaction();
            log.error(e.getMessage());
            throw e;
        } finally {
            JPAHelper.closeEntityManagerFactory();
        }
    }


    /**
     * Returns the next grade if the current grade is not the final one.
     *
     * @param currentGrade the current grade of the class group.
     * @return an Optional containing the next grade, or an empty Optional if the current grade is the final grade.
     */
    private Optional<Grade> getNextGradeIfNotFinal(Grade currentGrade) {
        Grade[] grades = Grade.values();
        int nextIndex = currentGrade.ordinal() + 1;
        if (nextIndex >= grades.length) {
            return Optional.empty();
        }
        return Optional.of(grades[nextIndex]);
    }



    /**
     * Archives all students in a ClassGroup and deletes the ClassGroup if all students are archived.
     *
     * @param classGroup the ClassGroup whose students should be archived and potentially deleted.
     */
    private void archiveStudentsAndDeleteClassGroup(ClassGroup classGroup) {
        classGroup.getStudents().forEach(student -> {
            student.setArchived(true);
            studentDAO.updateStudent(student);
            log.info("Student with ID " + student.getId() + " has been archived.");
        });

        classGroupDAO.deleteClassGroup(classGroup.getId());
        log.info("ClassGroup with ID " + classGroup.getId() + " has been deleted because all students have graduated.");
    }
}
