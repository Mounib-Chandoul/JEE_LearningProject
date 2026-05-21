package com.example.jee_project;

import com.example.jee_project.Entity.Student;
import com.example.jee_project.Repository.StudentRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * REST Resource for managing Student records.
 */
@Path("/students")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    @Inject
    private StudentRepository studentRepository;

    /**
     * Retrieves all student records from the database.
     * @return A list of all Students.
     */
    @GET
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Creates a new student record or updates an existing one.
     * @param student The Student object to be saved.
     * @return A Response with the saved student and status 201 (Created).
     */
    @POST
    public Response createStudent(Student student) {
        Student saved = studentRepository.save(student);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    /**
     * Deletes a student record based on its unique identifier.
     * @param id The ID of the student to delete.
     * @return A Response with status 204 (No Content) if successful.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        studentRepository.delete(id);
        return Response.noContent().build();
    }
}
