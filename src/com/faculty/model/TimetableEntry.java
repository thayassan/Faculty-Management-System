package com.faculty.model;

public class TimetableEntry {

    private int timetableId;
    private int courseId;
    private String courseCode; // convenience field populated by joins for display
    private String dayOfWeek;  // Monday .. Friday
    private String timeSlot;   // e.g. "08.00", "10.00"

    public TimetableEntry() {
    }

    public TimetableEntry(int timetableId, int courseId, String courseCode,
                           String dayOfWeek, String timeSlot) {
        this.timetableId = timetableId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.dayOfWeek = dayOfWeek;
        this.timeSlot = timeSlot;
    }

    public int getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(int timetableId) {
        this.timetableId = timetableId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}
