package com.faculty.model;

/**
 * Represents a login account. role is one of ADMIN, STUDENT, LECTURER.
 * linkedId points to the students.student_id or lecturers.lecturer_id
 * row that this account belongs to (null for ADMIN).
 */
public class User {

    private int userId;
    private String username;
    private String password;
    private String role;
    private Integer linkedId;

    public User() {
    }

    public User(int userId, String username, String password, String role, Integer linkedId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedId = linkedId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(Integer linkedId) {
        this.linkedId = linkedId;
    }

    @Override
    public String toString() {
        return username + " (" + role + ")";
    }
}
