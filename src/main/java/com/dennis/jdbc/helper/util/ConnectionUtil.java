package com.dennis.jdbc.helper.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionUtil {
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
