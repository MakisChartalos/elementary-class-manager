package gr.aueb.cf.elementaryclassmanager.mapper;

import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.model.Student;
import gr.aueb.cf.elementaryclassmanager.model.ClassGroup;

/**
 * Utility class for mapping between entity classes and their corresponding Data Transfer Objects (DTOs).
 * This class provides static methods to convert between DTOs and entity objects for teachers, students, and class groups.
 */
public class Mapper {

    private Mapper() {
        // Prevent instantiation of the utility class
    }

    // --- Teacher Mappings ---

    /**
     * Maps a TeacherInsertDTO to a Teacher entity.
     *
     * @param dto the TeacherInsertDTO containing data to create a Teacher entity
     * @return a new Teacher object populated with data from the given DTO
     */
    public static Teacher mapToTeacher(TeacherInsertDTO dto) {
        return new Teacher(dto.getFirstname(), dto.getLastname(), dto.getRegistrationNumber(), dto.getEmail());
    }

    /**
     * Updates an existing Teacher entity with data from a TeacherUpdateDTO.
     *
     * @param teacher the existing Teacher entity to be updated
     * @param dto     the TeacherUpdateDTO containing updated data
     * @return the updated Teacher entity
     */
    public static Teacher mapToTeacher(Teacher teacher, TeacherUpdateDTO dto) {
        if (dto.getFirstname() != null) {
            teacher.setFirstname(dto.getFirstname());
        }
        if (dto.getLastname() != null) {
            teacher.setLastname(dto.getLastname());
        }

        if (dto.getEmail() != null) {
            teacher.setEmail(dto.getEmail());
        }
        return teacher;
    }

    /**
     * Maps a Teacher entity to a TeacherReadOnlyDTO.
     *
     * @param teacher the Teacher entity to be mapped
     * @return a TeacherReadOnlyDTO containing the teacher's details
     */
    public static TeacherReadOnlyDTO mapToTeacherReadOnlyDTO(Teacher teacher) {
        return new TeacherReadOnlyDTO(
                teacher.getId(),
                teacher.getFirstname(),
                teacher.getLastname(),
                teacher.getRegistrationNumber(),
                teacher.getEmail()
        );
    }

    // --- Student Mappings ---

    /**
     * Maps a StudentInsertDTO to a Student entity.
     *
     * @param dto the StudentInsertDTO containing data to create a Student entity
     * @return a new Student object populated with data from the given DTO
     */
    public static Student mapToStudent(StudentInsertDTO dto) {
        return new Student(dto.getFirstname(), dto.getLastname(),dto.getSsn(), dto.getGrade());
    }

    /**
     * Updates an existing Student entity with data from a StudentUpdateDTO.
     *
     * @param student the existing Student entity to be updated
     * @param dto     the StudentUpdateDTO containing updated data
     * @return the updated Student entity
     */
    public static Student mapToStudent(Student student, StudentUpdateDTO dto) {
        if (dto.getFirstname() != null) {
            student.setFirstname(dto.getFirstname());
        }
        if (dto.getLastname() != null) {
            student.setLastname(dto.getLastname());
        }
        if (dto.getGrade() != null) {
            student.setGrade(dto.getGrade());
        }
        return student;
    }

    /**
     * Maps a Student entity to a StudentReadOnlyDTO.
     *
     * @param student the Student entity to be mapped
     * @return a StudentReadOnlyDTO containing the student's details
     */
    public static StudentReadOnlyDTO mapToStudentReadOnlyDTO(Student student) {
        StudentReadOnlyDTO dto = new StudentReadOnlyDTO(student.getFirstname(), student.getLastname(),  student.getSsn(), student.getGrade(), student.getClassGroup().getId());
        dto.setId(student.getId());
        return dto;
    }

    // --- ClassGroup Mappings ---

    /**
     * Maps a ClassGroupInsertDTO to a ClassGroup entity.
     *
     * @param dto the ClassGroupInsertDTO containing data to create a ClassGroup entity
     * @return a new ClassGroup object populated with data from the given DTO
     */
    public static ClassGroup mapToClassGroup(ClassGroupInsertDTO dto) {
        return new ClassGroup(dto.getName(), dto.getGrade());
    }

    /**
     * Updates an existing ClassGroup entity with data from a ClassGroupUpdateDTO.
     *
     * @param classGroup the existing ClassGroup entity to be updated
     * @param dto        the ClassGroupUpdateDTO containing updated data
     * @return the updated ClassGroup entity
     */
    public static ClassGroup mapToClassGroup(ClassGroup classGroup, ClassGroupUpdateDTO dto) {
        if (dto.getName() != null) {
            classGroup.setName(dto.getName());
        }
        if (dto.getGrade() != null) {
            classGroup.setGrade(dto.getGrade());
        }
        return classGroup;
    }

    /**
     * Maps a ClassGroup entity to a ClassGroupReadOnlyDTO.
     *
     * @param classGroup the ClassGroup entity to be mapped
     * @return a ClassGroupReadOnlyDTO containing the class group's details
     */
    public static ClassGroupReadOnlyDTO mapToClassGroupReadOnlyDTO(ClassGroup classGroup) {
        return new ClassGroupReadOnlyDTO(classGroup.getName(), classGroup.getGrade());
    }
}
