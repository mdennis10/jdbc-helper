package com.dennis.jdbc.helper.util;

import com.dennis.jdbc.helper.exception.HelperSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtil {
    private static final Logger LOGGER;
    static {
        LOGGER = Logger.getLogger(ConnectionUtil.class.getName());
        LOGGER.setLevel(Level.WARNING);
    }

    public static void close(Connection connection) {
        if (isClosed(connection))
            return;
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet == null)
            return;
        try {
            resultSet.close();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    public static void close(PreparedStatement statement) {
        if(statement == null)
            return;
        try {
            statement.close();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    public static boolean isClosed(Connection connection) {
        if (connection == null)
            return true;
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    public static boolean isClosed(ResultSet resultSet) {
        if (resultSet == null)
            return true;
        try {
            return resultSet.isClosed();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }
}
