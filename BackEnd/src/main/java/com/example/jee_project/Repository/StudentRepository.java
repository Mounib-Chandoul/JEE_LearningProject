package com.example.jee_project.Repository;

import com.example.jee_project.Entity.Student;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * Repository for managing Student persistence and CRUD operations.
 */
@Stateless
public class StudentRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all student records from the database using JPQL.
     * @return A list of all Student entities.
     */
    public List<Student> findAll() {
        return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
    }

    /**
     * Persists a new student or updates an existing one using merge.
     * @param student The Student entity to be saved.
     * @return The managed Student entity.
     */
    public Student save(Student student) {
        if (student.getId() == null) {
            em.persist(student);
            return student;
        } else {
            return em.merge(student);
        }
    }

    /**
     * Removes a student from the database based on their ID.
     * @param id The ID of the student to remove.
     */
    public void delete(Long id) {
        Student student = em.find(Student.class, id);
        if (student != null) {
            em.remove(student);
        }
    }
}
