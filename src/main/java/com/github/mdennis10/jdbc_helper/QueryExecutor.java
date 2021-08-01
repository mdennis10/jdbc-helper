package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class QueryExecutor {
    protected  <T> Optional<T> query(boolean isAutoClose, @NotNull Connection connection, @NotNull Class<T> clazz, @NotNull String sql, @NotNull Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(clazz, "Null clazz argument supplied");
        Preconditions.checkNotNull(arguments,"Null SQL parameter arguments supplied" );
        try {
            if(isAutoClose) {
                try(Connection conn = connection;
                    PreparedStatement stmt = conn.prepareStatement(sql)){
                    return execute(clazz, stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return execute(clazz, stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    private <T> Optional<T> execute(Class<T> clazz, PreparedStatement stmt, Object[] arguments) throws SQLException {
        ExecutorHelperUtil.resolveParameters(stmt, arguments);
        try(ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                Map<String, Object> dataResult = ExecutorHelperUtil.parseRow(resultSet);
                T resolved = ReflectiveTypeResolver.resolve(clazz, dataResult);
                return (resolved != null) ? Optional.of(resolved) : Optional.empty();
            }
        }
        return Optional.empty();
    }
}
