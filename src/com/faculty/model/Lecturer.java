package com.faculty.model;

public class Lecturer {

    private int lecturerId;
    private String fullName;
    private int deptId;
    private String deptName; // convenience field populated by joins for display
    private String email;
    private String mobile;
    private String coursesTeaching = ""; // convenience field populated by joins for display

    public Lecturer() {
    }

    public Lecturer(int lecturerId, String fullName, int deptId, String deptName,
                     String email, String mobile) {
        this.lecturerId = lecturerId;
        this.fullName = fullName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.email = email;
        this.mobile = mobile;
    }

    public int getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(int lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getCoursesTeaching() {
        return coursesTeaching;
    }

    public void setCoursesTeaching(String coursesTeaching) {
        this.coursesTeaching = coursesTeaching;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
