package gr.aueb.cf.elementaryclassmanager.service;

import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.TeacherAlreadyExists;

import java.util.List;


public interface ITeacherService {

    TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO dto) throws TeacherAlreadyExists;
    TeacherReadOnlyDTO updateTeacher(TeacherUpdateDTO dto) throws EntityNotFoundException;
    void deleteTeacher(Long id) throws EntityNotFoundException;
    List<TeacherReadOnlyDTO> getTeachersByLastname(String lastname) throws EntityNotFoundException;
    TeacherReadOnlyDTO getTeacherById(Long id) throws EntityNotFoundException;
}
