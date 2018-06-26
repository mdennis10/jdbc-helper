package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.exception.UnsupportedTypeException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.Date;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class SQLExecutor {

    protected static void parseParameters(PreparedStatement statement, Object... params) throws SQLException {
        if (params.length == 0)
            return;
        for (int index = 0; index < params.length; index++) {
            Object param = params[index];
            if(param == null) continue;// skips parameters

            if(param.getClass() == Integer.class) {
                statement.setInt(++index, (Integer) param);
            } else if(param.getClass() == Double.class) {
                statement.setDouble(++index, (Double) param);
            } else if(param.getClass() == String.class) {
                statement.setString(++index, (String) param);
            } else if(param.getClass() == Float.class) {
                statement.setFloat(++index, (Float) param);
            } else if(param.getClass() == Boolean.class) {
                statement.setBoolean(++index, (Boolean) param);
            } else if(param.getClass() == Long.class) {
                statement.setLong(++index,(Long) param);
            } else if(param.getClass() == Short.class) {
                statement.setShort(++index, (Short) param);
            } else if(param.getClass() == Character.class) {
                statement.setString(++index, String.valueOf(param));
            } else if(param.getClass() == Date.class) {
                statement.setTimestamp(++index, new Timestamp(((Date)param).getTime()));
            } else if(param.getClass() == Byte.class) {
                statement.setByte(++index, (Byte) param);
            } else {
                throw new UnsupportedTypeException(param.getClass());
            }
        }
    }

    protected ExecutionResult execute(Connection connection, String sql, Object... params) {
        Preconditions.checkArgument(connection != null, "Connection instance required");
        if (ConnectionUtil.isClosed(connection))
            throw new ConnectionException("Connection already closed");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            parseParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();
            return new ExecutionResult(connection, statement, resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected int executeUpdate(Connection connection, String sql, Object... params) {
        Preconditions.checkArgument(connection != null, "Connection instance required");
        if (ConnectionUtil.isClosed(connection))
            throw new ConnectionException("Connection already closed");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            parseParameters(statement, params);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
