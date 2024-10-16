package gr.aueb.cf.elementaryclassmanager.rest;

import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentInsertDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentReadOnlyDTO;
import gr.aueb.cf.elementaryclassmanager.dto.studentDTO.StudentUpdateDTO;
import gr.aueb.cf.elementaryclassmanager.service.IStudentService;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.elementaryclassmanager.service.exceptions.StudentAlreadyExists;
import gr.aueb.cf.elementaryclassmanager.validator.ValidatorUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.List;

/**
 * REST controller for managing students.
 * Provides endpoints for creating, updating, deleting, and retrieving student information.
 */
@Path("/students")
public class StudentRestController {

    @Inject
    private IStudentService studentService;

    /**
     * Adds a new student.
     *
     * @param dto      the data transfer object containing student details
     * @param uriInfo  the URI information to build the location header for the created resource
     * @return a Response indicating the outcome of the operation
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudent(StudentInsertDTO dto, @Context UriInfo uriInfo) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        try {
            StudentReadOnlyDTO readOnlyDTO = studentService.insertStudent(dto);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(Long.toString(readOnlyDTO.getId()));
            return Response.created(uriBuilder.build())
                    .entity(readOnlyDTO)
                    .build();
        } catch (StudentAlreadyExists e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    /**
     * Updates an existing student.
     *
     * @param id  the ID of the student to update
     * @param dto the data transfer object containing updated student details
     * @return a Response indicating the outcome of the operation
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStudent(@PathParam("id") Long id, StudentUpdateDTO dto) {
        List<String> errors = ValidatorUtil.validateDTO(dto);

        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }

        dto.setId(id);

        try {
            StudentReadOnlyDTO readOnlyDTO = studentService.updateStudent(dto);
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Deletes a student by ID.
     *
     * @param id  the ID of the student to delete
     * @return a Response indicating the outcome of the operation
     */
    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteStudent(@PathParam("id") Long id) {
        try {
            StudentReadOnlyDTO readOnlyDTO = studentService.getStudentById(id);
            studentService.deleteStudent(readOnlyDTO.getId());
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a student by ID.
     *
     * @param id  the ID of the student to retrieve
     * @return a Response containing the student details
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentsById(@PathParam("id") Long id) {
        try {
            StudentReadOnlyDTO readOnlyDTO = studentService.getStudentById(id);
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Searches for students by their last name.
     *
     * @param lastname  the last name of the students to search for
     * @return a Response containing a list of students that match the last name
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentsByLastname(@QueryParam("lastname") String lastname) {
        if (lastname == null || lastname.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            List<StudentReadOnlyDTO> students = studentService.getStudentByLastname(lastname);
            return Response.ok().entity(students).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Retrieves a student by their Social Security Number (SSN).
     *
     * @param ssn  the SSN of the student to retrieve
     * @return a Response containing the student details
     */
    @Path("/ssn/{ssn}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentsBySsn(@PathParam("ssn") String ssn) {
        if (ssn == null || ssn.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            StudentReadOnlyDTO readOnlyDTO = studentService.getStudentBySsn(ssn);
            return Response.ok().entity(readOnlyDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
