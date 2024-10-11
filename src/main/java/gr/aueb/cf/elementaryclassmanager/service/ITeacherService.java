package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.TeacherAlreadyExists;

import java.util.List;
import java.util.Optional;

public interface ITeacherService {

    Teacher insertTeacher(TeacherInsertDTO dto) throws TeacherAlreadyExists;
    Teacher updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException;
    void deleteTeacher(Long id) throws EntityNotFoundException;
    List<TeacherReadOnlyDTO> getTeachersByLastname(String lastname) throws EntityNotFoundException;
    TeacherReadOnlyDTO getTeacherById(Long id) throws EntityNotFoundException;
}
