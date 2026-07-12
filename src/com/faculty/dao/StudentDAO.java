package com.faculty.dao;

import com.faculty.model.Student;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for the students table, plus joined lookups for the
 * "Courses Enrolled" and "Time table" tabs on the student dashboard.
 *
 * NOTE: Karushman's module owns Course/Enrollment/TimetableEntry models
 * and their DAOs. Once those exist, getEnrollments()/getTimetable() below
 * can be swapped to call EnrollmentDAO/TimetableDAO instead of querying
 * the tables directly here. For now these small inner row classes
 * (CourseGradeRow, TimetableSlot) keep this file self-contained and avoid
 * any class-name clashes with his upcoming Enrollment.java/TimetableEntry.java.
 */
public class StudentDAO {

    /** One row for the "Courses Enrolled" table: code, name, credits, grade. */
    public static class CourseGradeRow {
        public final String courseCode;
        public final String courseName;
        public final int credits;
        public final String grade;

        public CourseGradeRow(String courseCode, String courseName, int credits, String grade) {
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
            this.grade = grade;
        }
    }

    /** One row for the "Time table" tab: day, time slot, course code. */
    public static class TimetableSlot {
        public final String dayOfWeek;
        public final String timeSlot;
        public final String courseCode;

        public TimetableSlot(String dayOfWeek, String timeSlot, String courseCode) {
            this.dayOfWeek = dayOfWeek;
            this.timeSlot = timeSlot;
            this.courseCode = courseCode;
        }
    }

    /**
     * Loads a student's profile, joined with the degree name for display.
     */
    public Student findById(int studentId) throws SQLException {
        String sql = "SELECT s.*, d.degree_name " +
                "FROM students s LEFT JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE s.student_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    /**
     * Updates the editable profile fields (name, degree, email, mobile).
     * studentNumber is treated as immutable once created.
     */
    public boolean update(Student student) throws SQLException {
        String sql = "UPDATE students SET full_name = ?, degree_id = ?, email = ?, mobile = ? " +
                "WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, student.getFullName());
            if (student.getDegreeId() != null) {
                ps.setInt(2, student.getDegreeId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getMobile());
            ps.setInt(5, student.getStudentId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    /**
     * Courses this student is enrolled in, with grade, for the
     * "Courses Enrolled" tab.
     */
    public List<CourseGradeRow> getEnrollments(int studentId) throws SQLException {
        String sql = "SELECT c.course_code, c.course_name, c.credits, e.grade " +
                "FROM enrollments e JOIN courses c ON e.course_id = c.course_id " +
                "WHERE e.student_id = ? ORDER BY c.course_code";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<CourseGradeRow> rows = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new CourseGradeRow(
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"),
                        rs.getString("grade")
                ));
            }
            return rows;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    /**
     * Weekly timetable slots for every course this student is enrolled in,
     * for the "Time table" tab. Ordered Monday-Friday, then by time.
     */
    public List<TimetableSlot> getTimetable(int studentId) throws SQLException {
        String sql = "SELECT t.day_of_week, t.time_slot, c.course_code " +
                "FROM enrollments e " +
                "JOIN courses c ON e.course_id = c.course_id " +
                "JOIN timetable t ON t.course_id = c.course_id " +
                "WHERE e.student_id = ? " +
                "ORDER BY FIELD(t.day_of_week, 'Monday','Tuesday','Wednesday','Thursday','Friday'), t.time_slot";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TimetableSlot> rows = new ArrayList<>();
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, studentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                rows.add(new TimetableSlot(
                        rs.getString("day_of_week"),
                        rs.getString("time_slot"),
                        rs.getString("course_code")
                ));
            }
            return rows;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getInt("student_id"));
        s.setStudentNumber(rs.getString("student_number"));
        s.setFullName(rs.getString("full_name"));
        int degreeId = rs.getInt("degree_id");
        s.setDegreeId(rs.wasNull() ? null : degreeId);
        s.setEmail(rs.getString("email"));
        s.setMobile(rs.getString("mobile"));
        try {
            s.setDegreeName(rs.getString("degree_name"));
        } catch (SQLException ignored) {
            // degree_name not present if this query variant didn't join it
        }
        return s;
    }
}
