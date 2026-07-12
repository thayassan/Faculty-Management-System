package com.faculty.controller;

import com.faculty.dao.UserDAO;
import com.faculty.model.User;

import java.sql.SQLException;

/**
 * Business logic for the login screen. Sits between LoginView and UserDAO.
 */
public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Attempts to log the user in.
     *
     * @param username plain username entered by the user
     * @param password plain password entered by the user
     * @param role     one of "ADMIN", "STUDENT", "LECTURER"
     * @return the authenticated User, or null if credentials are invalid
     * @throws SQLException if a database error occurs
     */
    public User login(String username, String password, String role) throws SQLException {
        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDAO.authenticate(username.trim(), password, role);
    }

    /**
     * Registers a new account. Used by the Sign Up tab.
     * For STUDENT/LECTURER roles, linkedId should reference the
     * corresponding students.student_id / lecturers.lecturer_id row
     * that was created for this person (created separately by an admin,
     * or left null if self-registration only creates the login record).
     */
    public boolean signUp(String username, String password, String role, Integer linkedId) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        if (userDAO.findByUsername(username.trim()) != null) {
            return false; // username already taken
        }
        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(password);
        u.setRole(role);
        u.setLinkedId(linkedId);
        return userDAO.insert(u);
    }
}
