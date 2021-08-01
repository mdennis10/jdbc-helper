package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryExecutor {
    protected <T> Optional<T> query(
            boolean isAutoClose,
            @NotNull Connection connection,
            @NotNull String sql,
            @NotNull Object[] arguments,
            @NotNull ColumnMapper<T> mapper) {
        Preconditions.checkNotNull(connection, "Null connection provided");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments,"Null SQL parameter arguments supplied" );
        Preconditions.checkNotNull(mapper, "Null mapper supplied");
        try {
            if(isAutoClose) {
                try(Connection conn = connection;
                    PreparedStatement stmt = conn.prepareStatement(sql)){
                    return executeQueryWithMapper(stmt, arguments, mapper);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeQueryWithMapper(stmt, arguments, mapper);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    protected <T> Optional<T> query(
            boolean isAutoClose,
            @NotNull Connection connection,
            @NotNull Class<T> clazz,
            @NotNull String sql,
            @NotNull Object[] arguments) {
        Preconditions.checkNotNull(connection, "Null connection provided");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(clazz, "Null clazz argument supplied");
        Preconditions.checkNotNull(arguments,"Null SQL parameter arguments supplied" );
        try {
            if(isAutoClose) {
                try(Connection conn = connection;
                    PreparedStatement stmt = conn.prepareStatement(sql)){
                    return executeQuery(clazz, stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeQuery(clazz, stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    protected <T> List<T> queryForList(
            boolean isAutoClose,
            @NotNull Connection connection,
            @NotNull Class<T> clazz,
            @NotNull String sql,
            @NotNull Object[] arguments) {
        Preconditions.checkNotNull(connection, "Null connection provided");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql),"Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null SQL parameter arguments supplied");
        Preconditions.checkNotNull(clazz, "Null clazz argument supplied");
        try {
            if(isAutoClose) {
                try (Connection conn = connection;
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return executeQueryForList(clazz, stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeQueryForList(clazz, stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    protected <T> List<T> queryForList(
            boolean isAutoClose,
            @NotNull Connection connection,
            @NotNull String sql,
            @NotNull Object[] arguments,
            @NotNull ColumnMapper<T> mapper) {
        Preconditions.checkNotNull(connection, "Null connection provided");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql),"Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null SQL parameter arguments supplied");
        Preconditions.checkNotNull(mapper, "Null mapper supplied");
        try {
            if(isAutoClose) {
                try (Connection conn = connection;
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return executeQueryForListWithMapper(stmt, arguments, mapper);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeQueryForListWithMapper(stmt, arguments, mapper);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    private <T> Optional<T> executeQueryWithMapper(PreparedStatement stmt, Object[] arguments, ColumnMapper<T> mapper) throws SQLException {
        ExecutorHelperUtil.resolveParameters(stmt, arguments);
        try(ResultSet resultSet = stmt.executeQuery()) {
            while (resultSet.next()) {
                T result = mapper.map(ExecutorHelperUtil.parseRow(resultSet));
                return (result != null) ? Optional.of(result) : Optional.empty();
            }
        }
        return Optional.empty();
    }

    private <T> Optional<T> executeQuery(Class<T> clazz, PreparedStatement stmt, Object[] arguments) throws SQLException {
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

    private <T> List<T> executeQueryForList(Class<T> clazz, PreparedStatement stmt, Object[] arguments) throws SQLException {
        ExecutorHelperUtil.resolveParameters(stmt, arguments);
        try(ResultSet resultSet = stmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while(resultSet.next()) {
                Map<String, Object> dataResult = ExecutorHelperUtil.parseRow(resultSet);
                result.add(ReflectiveTypeResolver.resolve(clazz, dataResult));
            }
            return result;
        }
    }

    private <T> List<T> executeQueryForListWithMapper(
            PreparedStatement stmt,
            Object[] arguments,
            ColumnMapper<T> mapper) throws SQLException {
        ExecutorHelperUtil.resolveParameters(stmt, arguments);
        try(ResultSet resultSet = stmt.executeQuery()) {
            List<T> result = new ArrayList<>();
            while(resultSet.next()) {
                T row = mapper.map(ExecutorHelperUtil.parseRow(resultSet));
                result.add(row);
            }
            return result;
        }
    }
}
