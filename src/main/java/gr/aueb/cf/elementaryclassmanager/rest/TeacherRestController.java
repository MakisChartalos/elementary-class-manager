package gr.aueb.cf.elementaryclassmanager.rest;

import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.teacherDTO.TeacherUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.mapper.Mapper;
import gr.aueb.cf.elementaryclassmanager.model.Teacher;
import gr.aueb.cf.elementaryclassmanager.service.ITeacherService;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.TeacherAlreadyExists;
import gr.aueb.cf.elementaryclassmanager.validator.ValidatorUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

/**
 * REST controller for managing teachers.
 * Provides endpoints for creating, updating, deleting, and retrieving teacher information.
 */
@Path("/teachers")
public class TeacherRestController {

    @Inject
    private ITeacherService teacherService;

    /**
     * Adds a new teacher.
     *
     * @param dto the data transfer object containing teacher details
     * @param uriInfo the URI information to build the location header for the created resource
     * @return a Response indicating the outcome of the operation
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTeacher(TeacherInsertDTO dto, @Context UriInfo uriInfo) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        try {
            Teacher teacher = teacherService.insertTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapToTeacherReadOnlyDTO(teacher);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(readOnlyDTO.getId()));
            return Response.created(uriBuilder.build())
                    .entity(readOnlyDTO)
                    .build();

        } catch (TeacherAlreadyExists e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing teacher.
     *
     * @param id the ID of the teacher to update
     * @param dto the data transfer object containing updated teacher details
     * @return a Response indicating the outcome of the operation
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("id") Long id, TeacherUpdateDTO dto) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        dto.setId(id);

        try {
            Teacher updatedTeacher = teacherService.updateTeacher(dto);
            TeacherReadOnlyDTO readOnlyDTO = Mapper.mapToTeacherReadOnlyDTO(updatedTeacher);
            return Response.ok(readOnlyDTO).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a teacher by ID.
     *
     * @param id the ID of the teacher to delete
     * @return a Response indicating the outcome of the operation
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteTeacher(@PathParam("id") Long id) {
        try {
            teacherService.deleteTeacher(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a teacher by ID.
     *
     * @param id the ID of the teacher to retrieve
     * @return a Response containing the teacher details
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacherById(@PathParam("id") Long id) {
        try {
            TeacherReadOnlyDTO teacherDTO = teacherService.getTeacherById(id);
            return Response.ok(teacherDTO).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Searches for teachers by their last name.
     *
     * @param lastname the last name of the teachers to search for
     * @return a Response containing a list of teachers that match the last name
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeachersByLastname(@QueryParam("lastname") String lastname) {
        if (lastname == null || lastname.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Lastname is required").build();
        }

        try {
            List<TeacherReadOnlyDTO> teachers = teacherService.getTeachersByLastname(lastname);
            return Response.ok(teachers).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}
