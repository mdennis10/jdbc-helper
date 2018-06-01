package com.dennis.jdbc.extension.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionUtil {
    private ConnectionUtil() {
    }

    public static void close(Connection connection) {
        if (isClosed(connection))
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet == null)
            return;
        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isClosed(Connection connection) {
        if (connection == null)
            return true;
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isClosed(ResultSet resultSet) {
        if (resultSet == null)
            return true;
        try {
            return resultSet.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
