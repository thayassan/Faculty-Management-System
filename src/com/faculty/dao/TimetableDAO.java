package com.faculty.dao;

import com.faculty.model.TimetableEntry;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimetableDAO {

    /**
     * Returns the timetable slots for every course a given student is
     * currently enrolled in.
     */
    public List<TimetableEntry> getForStudent(int studentId) throws SQLException {
        String sql = "SELECT t.*, c.course_code FROM timetable t " +
                "JOIN courses c ON t.course_id = c.course_id " +
                "JOIN enrollments e ON e.course_id = c.course_id " +
                "WHERE e.student_id = ? " +
                "ORDER BY FIELD(t.day_of_week, 'Monday','Tuesday','Wednesday','Thursday','Friday'), t.time_slot";
        return runQuery(sql, studentId);
    }

    /**
     * Returns the timetable slots for every course a given lecturer teaches.
     */
    public List<TimetableEntry> getForLecturer(int lecturerId) throws SQLException {
        String sql = "SELECT t.*, c.course_code FROM timetable t " +
                "JOIN courses c ON t.course_id = c.course_id " +
                "WHERE c.lecturer_id = ? " +
                "ORDER BY FIELD(t.day_of_week, 'Monday','Tuesday','Wednesday','Thursday','Friday'), t.time_slot";
        return runQuery(sql, lecturerId);
    }

    private List<TimetableEntry> runQuery(String sql, int id) throws SQLException {
        List<TimetableEntry> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return list;
    }

    public boolean insert(TimetableEntry t) throws SQLException {
        String sql = "INSERT INTO timetable (course_id, day_of_week, time_slot) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getCourseId());
            ps.setString(2, t.getDayOfWeek());
            ps.setString(3, t.getTimeSlot());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean delete(int timetableId) throws SQLException {
        String sql = "DELETE FROM timetable WHERE timetable_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, timetableId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    private TimetableEntry mapRow(ResultSet rs) throws SQLException {
        TimetableEntry t = new TimetableEntry();
        t.setTimetableId(rs.getInt("timetable_id"));
        t.setCourseId(rs.getInt("course_id"));
        t.setCourseCode(rs.getString("course_code"));
        t.setDayOfWeek(rs.getString("day_of_week"));
        t.setTimeSlot(rs.getString("time_slot"));
        return t;
    }
}
