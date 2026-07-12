package com.faculty.model;

public class Degree {

    private int degreeId;
    private String degreeName;
    private int deptId;
    private String deptName; // convenience field populated by joins for display
    private int studentCount;

    public Degree() {
    }

    public Degree(int degreeId, String degreeName, int deptId, String deptName, int studentCount) {
        this.degreeId = degreeId;
        this.degreeName = degreeName;
        this.deptId = deptId;
        this.deptName = deptName;
        this.studentCount = studentCount;
    }

    public int getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(int degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
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

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    @Override
    public String toString() {
        return degreeName;
    }
}
