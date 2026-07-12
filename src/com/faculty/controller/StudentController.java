package com.faculty.controller;

import com.faculty.dao.StudentDAO;
import com.faculty.model.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Business logic for the Student dashboard. Sits between
 * StudentDashboardView and StudentDAO.
 */
public class StudentController {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final StudentDAO studentDAO = new StudentDAO();

    /**
     * Loads the profile for the currently logged-in student.
     * studentId comes from users.linked_id for a STUDENT-role account.
     */
    public Student getProfile(int studentId) throws SQLException {
        return studentDAO.findById(studentId);
    }

    /**
     * Validates and saves profile edits (name, degree, email, mobile).
     *
     * @return null if saved successfully, or an error message to show the user.
     */
    public String saveProfile(Student student) throws SQLException {
        if (student.getFullName() == null || student.getFullName().trim().isEmpty()) {
            return "Full name cannot be empty.";
        }
        if (student.getEmail() != null && !student.getEmail().trim().isEmpty()
                && !EMAIL_PATTERN.matcher(student.getEmail().trim()).matches()) {
            return "Please enter a valid email address.";
        }
        boolean updated = studentDAO.update(student);
        return updated ? null : "No changes were saved. Please try again.";
    }

    public List<StudentDAO.CourseGradeRow> getEnrollments(int studentId) throws SQLException {
        return studentDAO.getEnrollments(studentId);
    }

    public List<StudentDAO.TimetableSlot> getTimetable(int studentId) throws SQLException {
        return studentDAO.getTimetable(studentId);
    }
}
