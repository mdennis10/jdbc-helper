package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class SQLExecutor {

    private static void parseParameters(PreparedStatement statement, String... params) throws SQLException {
        if (params.length == 0)
            return;
        for (int index = 0; index < params.length; index++) {
            statement.setString(index + 1, params[index]);
        }
    }

    protected ExecutionResult execute(Connection connection, String sql, String... params) {
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

    protected int executeUpdate(Connection connection, String sql, String... params) {
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
