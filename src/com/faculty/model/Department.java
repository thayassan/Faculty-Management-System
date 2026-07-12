package com.faculty.model;

public class Department {

    private int deptId;
    private String name;
    private String hodName;
    private String degreeName; // primary/flagship degree label shown in mockup
    private int staffCount;

    public Department() {
    }

    public Department(int deptId, String name, String hodName, String degreeName, int staffCount) {
        this.deptId = deptId;
        this.name = name;
        this.hodName = hodName;
        this.degreeName = degreeName;
        this.staffCount = staffCount;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public int getStaffCount() {
        return staffCount;
    }

    public void setStaffCount(int staffCount) {
        this.staffCount = staffCount;
    }

    @Override
    public String toString() {
        return name;
    }
}
