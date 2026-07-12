package com.faculty.dao;

import com.faculty.model.Degree;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DegreeDAO {

    private static final String SELECT_JOIN =
            "SELECT deg.*, dep.name AS dept_name FROM degrees deg " +
            "LEFT JOIN departments dep ON deg.dept_id = dep.dept_id ";

    public List<Degree> getAll() throws SQLException {
        String sql = SELECT_JOIN + "ORDER BY deg.degree_name";
        List<Degree> list = new ArrayList<>();
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

    public Degree getById(int degreeId) throws SQLException {
        String sql = SELECT_JOIN + "WHERE deg.degree_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, degreeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    public boolean insert(Degree d) throws SQLException {
        String sql = "INSERT INTO degrees (degree_name, dept_id, student_count) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, d.getDegreeName());
            ps.setInt(2, d.getDeptId());
            ps.setInt(3, d.getStudentCount());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean update(Degree d) throws SQLException {
        String sql = "UPDATE degrees SET degree_name = ?, dept_id = ?, student_count = ? WHERE degree_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, d.getDegreeName());
            ps.setInt(2, d.getDeptId());
            ps.setInt(3, d.getStudentCount());
            ps.setInt(4, d.getDegreeId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean delete(int degreeId) throws SQLException {
        String sql = "DELETE FROM degrees WHERE degree_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, degreeId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    private Degree mapRow(ResultSet rs) throws SQLException {
        Degree d = new Degree();
        d.setDegreeId(rs.getInt("degree_id"));
        d.setDegreeName(rs.getString("degree_name"));
        d.setDeptId(rs.getInt("dept_id"));
        d.setDeptName(rs.getString("dept_name"));
        d.setStudentCount(rs.getInt("student_count"));
        return d;
    }
}
