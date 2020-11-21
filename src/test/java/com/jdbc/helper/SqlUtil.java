package com.jdbc.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlUtil {
    public static int executeUpdate(DbConfig config, String sql) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            Class.forName(config.getDriverClassName());
            conn = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
            stmt = conn.prepareStatement(sql);
            return stmt.executeUpdate();
        }  finally {
            if(conn != null) conn.close();
            if(stmt != null) stmt.close();
        }
    }
}
