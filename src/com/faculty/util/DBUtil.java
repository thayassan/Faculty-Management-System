package com.faculty.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Small helper for safely closing JDBC resources without repeating
 * try/catch blocks in every DAO method.
 */
public class DBUtil {

    private DBUtil() {
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) {
        close(rs);
        close(ps);
        close(conn);
    }
}
