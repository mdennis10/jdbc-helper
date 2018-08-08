package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.exception.HelperSQLException;
import com.dennis.jdbc.helper.exception.UnsupportedTypeException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.google.common.base.Preconditions;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SQLExecutor {
    private static final Logger LOGGER = Logger.getLogger(SQLExecutor.class.getName());

    SQLExecutor() {
        LOGGER.setLevel(Level.WARNING);
    }

    protected static void parseParameters(PreparedStatement statement, Object... params) throws SQLException {
        if (params.length == 0)
            return;
        for (int index = 0; index < params.length; index++) {
            Object param = params[index];
            if(param == null) continue;// skips parameters when null

            if(param.getClass() == Integer.class) {
                statement.setInt(index + 1, (Integer) param);
            } else if(param.getClass() == Double.class) {
                statement.setDouble(index + 1, (Double) param);
            } else if(param.getClass() == String.class) {
                statement.setString(index + 1, (String) param);
            } else if(param.getClass() == Float.class) {
                statement.setFloat(index + 1, (Float) param);
            } else if(param.getClass() == Boolean.class) {
                statement.setBoolean(index + 1, (Boolean) param);
            } else if(param.getClass() == Long.class) {
                statement.setLong(index + 1,(Long) param);
            } else if(param.getClass() == Short.class) {
                statement.setShort(index + 1, (Short) param);
            } else if(param.getClass() == Character.class) {
                statement.setString(index + 1, String.valueOf(param));
            } else if(param.getClass() == java.util.Date.class) {
                throw new UnsupportedTypeException("java.util.Date is not supported use java.sql.Date instead");
            } else if(param.getClass() == Date.class) {
                statement.setDate(index + 1, (Date) param);
            } else if(param.getClass() == Byte.class) {
                statement.setByte(index + 1, (Byte) param);
            }  else {
                UnsupportedTypeException exception = new UnsupportedTypeException(param.getClass());
                LOGGER.severe(exception.getMessage());
                throw exception;
            }
        }
    }

    protected ExecutionResult execute(Connection connection, String sql, Object... params) {
        Preconditions.checkArgument(connection != null, "Connection instance required");
        if (ConnectionUtil.isClosed(connection)){
            ConnectionException exception = new ConnectionException("Connection already closed");
            LOGGER.severe(exception.getMessage());
            throw exception;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            parseParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            return new ExecutionResult(connection, statement, resultSet);
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    protected UpdateExecutionResult executeUpdate(Connection connection, String sql, Object... params) {
        Preconditions.checkArgument(connection != null, "Connection instance required");
        if (ConnectionUtil.isClosed(connection)) {
            ConnectionException exception = new ConnectionException("Connection already closed");
            LOGGER.severe(exception.getMessage());
            throw exception;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            parseParameters(statement, params);
            return new UpdateExecutionResult(connection, statement,null, statement.executeUpdate());
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }
}
