package com.faculty.controller;

import com.faculty.dao.CourseDAO;
import com.faculty.dao.LecturerDAO;
import com.faculty.dao.TimetableDAO;
import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.model.TimetableEntry;

import java.sql.SQLException;
import java.util.List;

public class LecturerController {

    private final LecturerDAO lecturerDAO = new LecturerDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final TimetableDAO timetableDAO = new TimetableDAO();

    public Lecturer getProfile(int lecturerId) throws SQLException {
        return lecturerDAO.getById(lecturerId);
    }

    public boolean updateProfile(Lecturer l) throws SQLException {
        if (l.getFullName() == null || l.getFullName().trim().isEmpty()) {
            return false;
        }
        return lecturerDAO.update(l);
    }

    public List<TimetableEntry> getTimetable(int lecturerId) throws SQLException {
        return timetableDAO.getForLecturer(lecturerId);
    }

    public List<Course> getTeachingCourses(int lecturerId) throws SQLException {
        return courseDAO.getByLecturer(lecturerId);
    }
}
