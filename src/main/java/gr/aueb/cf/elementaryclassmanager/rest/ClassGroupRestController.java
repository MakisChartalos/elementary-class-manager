package gr.aueb.cf.elementaryclassmanager.rest;

import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.classgroupDTO.ClassGroupUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.service.IClassGroupService;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.*;
import gr.aueb.cf.elementaryclassmanager.validator.ValidatorUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

/**
 * REST controller for managing class groups.
 * Provides endpoints for creating, updating, deleting, and retrieving class groups,
 * as well as managing associations with students and teachers.
 */
@Path("/classgroups")
public class ClassGroupRestController {

    @Inject
    private IClassGroupService classGroupService;

    /**
     * Adds a new class group.
     *
     * @param dto      the data transfer object containing the class group details.
     * @param uriInfo  the URI info used to build the created resource's URI.
     * @return a Response containing the created class group or an error message.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addClassGroup(ClassGroupInsertDTO dto, @Context UriInfo uriInfo) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        try {
            ClassGroupReadOnlyDTO readOnlyDTO = classGroupService.insertClassGroup(dto);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(readOnlyDTO.getId()));
            return Response.created(uriBuilder.build())
                    .entity(readOnlyDTO)
                    .build();
        } catch (ClassGroupAlreadyExists e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing class group by its ID.
     *
     * @param id   the ID of the class group to update.
     * @param dto  the data transfer object containing the updated class group details.
     * @return a Response containing the updated class group or an error message.
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClassGroup(@PathParam("id") Long id, ClassGroupUpdateDTO dto) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        dto.setId(id);

        try {
            ClassGroupReadOnlyDTO readOnlyDTO = classGroupService.updateClassGroup(dto);
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes an existing class group by its ID.
     *
     * @param id the ID of the class group to delete.
     * @return a Response containing the deleted class group or an error message.
     */
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteClassGroup(@PathParam("id") Long id) {
        try {
            ClassGroupReadOnlyDTO readOnlyDTO = classGroupService.getClassGroupById(id);
            classGroupService.deleteClassGroup(readOnlyDTO.getId());
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves all students in a specific class group by the class group ID.
     *
     * @param classGroupId the ID of the class group.
     * @return a Response containing the list of students or an error message.
     */
    @Path("/{classGroupId}/students")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentsByClassGroupId(@PathParam("classGroupId") Long classGroupId) {
        try {
            List<StudentReadOnlyDTO> students = classGroupService.getStudentsByClassGroupId(classGroupId);
            return Response.ok().entity(students).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Assigns a student to a class group.
     *
     * @param classGroupId the ID of the class group.
     * @param studentId    the ID of the student to assign.
     * @return a Response indicating the outcome of the operation.
     */
    @Path("/{classGroupId}/assign-student/{studentId}")
    @PUT
    public Response assignStudentToClassGroup(@PathParam("classGroupId") Long classGroupId, @PathParam("studentId") Long studentId) {
        try {
            classGroupService.assignStudentToClassGroup(studentId, classGroupId);
            return Response.noContent().build();
        } catch (EntityNotFoundException | ClassGroupFullException | StudentGradeDoesNotMatchClassGroup e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Assigns a teacher to a class group.
     *
     * @param classGroupId the ID of the class group.
     * @param teacherId    the ID of the teacher to assign.
     * @return a Response indicating the outcome of the operation.
     */
    @Path("/{classGroupId}/assign-teacher/{teacherId}")
    @PUT
    public Response assignTeacherToClassGroup(@PathParam("classGroupId") Long classGroupId, @PathParam("teacherId") Long teacherId) {
        try {
            classGroupService.assignTeacherToClassGroup(teacherId, classGroupId);
            return Response.noContent().build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Increments the grade for all students in a specific class group.
     *
     * @param classGroupId the ID of the class group.
     * @return a Response indicating the outcome of the operation.
     */
    @Path("/{classGroupId}/increment-grade")
    @PUT
    public Response incrementGradeForClassGroup(@PathParam("classGroupId") Long classGroupId) {
        try {
            classGroupService.incrementGradeForClassGroup(classGroupId);
            return Response.noContent().build();
        } catch (EntityNotFoundException | GradeIncrementNotAllowedException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
