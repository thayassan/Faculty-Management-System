package com.faculty.dao;

import com.faculty.model.Course;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    private static final String SELECT_JOIN =
            "SELECT c.*, l.full_name AS lecturer_name FROM courses c " +
            "LEFT JOIN lecturers l ON c.lecturer_id = l.lecturer_id ";

    public List<Course> getAll() throws SQLException {
        String sql = SELECT_JOIN + "ORDER BY c.course_code";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return list;
    }

    public List<Course> getByLecturer(int lecturerId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE c.lecturer_id = ? ORDER BY c.course_code";
        List<Course> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, lecturerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
        return list;
    }

    public Course getById(int courseId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE c.course_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    public boolean insert(Course c) throws SQLException {
        String sql = "INSERT INTO courses (course_code, course_name, credits, lecturer_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            if (c.getLecturerId() != null) {
                ps.setInt(4, c.getLecturerId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean update(Course c) throws SQLException {
        String sql = "UPDATE courses SET course_code = ?, course_name = ?, credits = ?, lecturer_id = ? WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, c.getCourseCode());
            ps.setString(2, c.getCourseName());
            ps.setInt(3, c.getCredits());
            if (c.getLecturerId() != null) {
                ps.setInt(4, c.getLecturerId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, c.getCourseId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean delete(int courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseId(rs.getInt("course_id"));
        c.setCourseCode(rs.getString("course_code"));
        c.setCourseName(rs.getString("course_name"));
        c.setCredits(rs.getInt("credits"));
        int lecturerId = rs.getInt("lecturer_id");
        c.setLecturerId(rs.wasNull() ? null : lecturerId);
        c.setLecturerName(rs.getString("lecturer_name"));
        return c;
    }
}
