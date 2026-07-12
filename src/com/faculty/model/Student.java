package com.faculty.model;

/**
 * Represents a row in the students table, plus the joined degree name
 * (from degrees.degree_name) for convenient display in the UI.
 */
public class Student {

    private int studentId;
    private String studentNumber;
    private String fullName;
    private Integer degreeId;
    private String degreeName; // joined from degrees table, not a real column on students
    private String email;
    private String mobile;

    public Student() {
    }

    public Student(int studentId, String studentNumber, String fullName,
                   Integer degreeId, String email, String mobile) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.degreeId = degreeId;
        this.email = email;
        this.mobile = mobile;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return fullName + " (" + studentNumber + ")";
    }
}
