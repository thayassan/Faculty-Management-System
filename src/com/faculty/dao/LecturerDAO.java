package com.faculty.dao;

import com.faculty.model.Lecturer;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {

    public List<Lecturer> getAll() throws SQLException {
        String sql = "SELECT l.*, dep.name AS dept_name, GROUP_CONCAT(c.course_code SEPARATOR ', ') AS courses_teaching FROM lecturers l " +
                     "LEFT JOIN departments dep ON l.dept_id = dep.dept_id " +
                     "LEFT JOIN courses c ON l.lecturer_id = c.lecturer_id " +
                     "GROUP BY l.lecturer_id " +
                     "ORDER BY l.full_name";
        List<Lecturer> list = new ArrayList<>();
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

    public Lecturer getById(int lecturerId) throws SQLException {
        String sql = "SELECT l.*, dep.name AS dept_name, GROUP_CONCAT(c.course_code SEPARATOR ', ') AS courses_teaching FROM lecturers l " +
                     "LEFT JOIN departments dep ON l.dept_id = dep.dept_id " +
                     "LEFT JOIN courses c ON l.lecturer_id = c.lecturer_id " +
                     "WHERE l.lecturer_id = ? " +
                     "GROUP BY l.lecturer_id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, lecturerId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    public boolean insert(Lecturer l) throws SQLException {
        String sql = "INSERT INTO lecturers (full_name, dept_id, email, mobile) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, l.getFullName());
            ps.setInt(2, l.getDeptId());
            ps.setString(3, l.getEmail());
            ps.setString(4, l.getMobile());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean update(Lecturer l) throws SQLException {
        String sql = "UPDATE lecturers SET full_name = ?, dept_id = ?, email = ?, mobile = ? WHERE lecturer_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, l.getFullName());
            ps.setInt(2, l.getDeptId());
            ps.setString(3, l.getEmail());
            ps.setString(4, l.getMobile());
            ps.setInt(5, l.getLecturerId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean delete(int lecturerId) throws SQLException {
        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, lecturerId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    private Lecturer mapRow(ResultSet rs) throws SQLException {
        Lecturer l = new Lecturer();
        l.setLecturerId(rs.getInt("lecturer_id"));
        l.setFullName(rs.getString("full_name"));
        l.setDeptId(rs.getInt("dept_id"));
        l.setDeptName(rs.getString("dept_name"));
        l.setEmail(rs.getString("email"));
        l.setMobile(rs.getString("mobile"));
        String ct = rs.getString("courses_teaching");
        l.setCoursesTeaching(ct == null ? "" : ct);
        return l;
    }
}
