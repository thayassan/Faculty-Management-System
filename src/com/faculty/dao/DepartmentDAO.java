package com.faculty.dao;

import com.faculty.model.Department;
import com.faculty.util.DBConnection;
import com.faculty.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public List<Department> getAll() throws SQLException {
        String sql = "SELECT * FROM departments ORDER BY name";
        List<Department> list = new ArrayList<>();
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

    public Department getById(int deptId) throws SQLException {
        String sql = "SELECT * FROM departments WHERE dept_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBUtil.closeAll(rs, ps, conn);
        }
    }

    public boolean insert(Department d) throws SQLException {
        String sql = "INSERT INTO departments (name, hod_name, degree_name, staff_count) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, d.getName());
            ps.setString(2, d.getHodName());
            ps.setString(3, d.getDegreeName());
            ps.setInt(4, d.getStaffCount());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean update(Department d) throws SQLException {
        String sql = "UPDATE departments SET name = ?, hod_name = ?, degree_name = ?, staff_count = ? WHERE dept_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, d.getName());
            ps.setString(2, d.getHodName());
            ps.setString(3, d.getDegreeName());
            ps.setInt(4, d.getStaffCount());
            ps.setInt(5, d.getDeptId());
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    public boolean delete(int deptId) throws SQLException {
        String sql = "DELETE FROM departments WHERE dept_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, deptId);
            return ps.executeUpdate() > 0;
        } finally {
            DBUtil.closeAll(null, ps, conn);
        }
    }

    private Department mapRow(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDeptId(rs.getInt("dept_id"));
        d.setName(rs.getString("name"));
        d.setHodName(rs.getString("hod_name"));
        d.setDegreeName(rs.getString("degree_name"));
        d.setStaffCount(rs.getInt("staff_count"));
        return d;
    }
}
