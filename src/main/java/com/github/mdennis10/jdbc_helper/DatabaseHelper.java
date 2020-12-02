package com.github.mdennis10.jdbc_helper;


import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class DatabaseHelper {
    private static ConnectionManager connectionManager;
    private final DbConfig config;
    public DatabaseHelper(DbConfig config) {
        this.config = config;
        connectionManager = HikariConnectionManager.getInstance();
    }

    /**
     * Get underline ConnectionManager used by DatabaseHelper.
     * @author Mario Dennis
     * @param connectionManager - a connection manager instance
     */
    public void setConnectionManager(ConnectionManager connectionManager) {
        DatabaseHelper.connectionManager = connectionManager;
    }

    private Connection getConnection(DbConfig config) {
        try {
            return connectionManager.getDataSource(config).getConnection();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }


    /**
     * Query database using given SQL data access statement provided.
     * @author Mario Dennis
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @param mapper - a callback that will extract results, one row at a time
     * @param <T> - entity class
     * @return single row of result extracted from mapper
     */
    public <T> Optional<T> query(@NotNull String sql, @NotNull Object[] arguments, @NotNull ColumnMapper<T> mapper) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql),"Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null SQL parameter arguments supplied");
        Preconditions.checkNotNull(mapper, "Null mapper supplied");
        try(Connection conn = getConnection(config);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            resolveParameters(stmt, arguments);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    T result = mapper.map(parseRow(resultSet));
                    return (result != null) ? Optional.of(result) : Optional.empty();
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }

    /**
     * Query database using given SQL data access statement provided.
     * @author Mario Dennis
     * @param clazz - entity class type
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @param <T> - entity class
     * @return instance of entity class with result row mapped
     */
    public <T> Optional<T> query(@NotNull Class<T> clazz,@NotNull String sql, @NotNull Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(clazz, "Null clazz argument supplied");
        Preconditions.checkNotNull(arguments,"Null SQL parameter arguments supplied" );
        try(Connection conn = getConnection(config);
            PreparedStatement stmt = conn.prepareStatement(sql)){
            resolveParameters(stmt, arguments);
            try(ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, Object> dataResult = parseRow(resultSet);
                    T resolved = ReflectiveTypeResolver.resolve(clazz, dataResult);
                    return (resolved != null) ? Optional.of(resolved) : Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
        return Optional.empty();
    }


    /**
     * Query database using given SQL data access statement provided.
     * @author Mario Dennis
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @param mapper - a callback that will extract results, one row at a time
     * @param <T> - entity class
     * @return rows of results extracted from mapper
     */
    public <T> List<T> queryForList(@NotNull String sql, @NotNull Object[] arguments, ColumnMapper<T> mapper){
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql),"Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null SQL parameter arguments supplied");
        Preconditions.checkNotNull(mapper, "Null mapper supplied");
        try (Connection conn = getConnection(config);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            resolveParameters(stmt, arguments);
            try (ResultSet resultSet = stmt.executeQuery())  {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    T row = mapper.map(parseRow(resultSet));
                    result.add(row);
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }

    /**
     * Query database using given SQL data access statement provided.
     * @author Mario Dennis
     * @param clazz - entity class type
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @param <T> - entity class
     * @return rows of results extracted from mapper
     */
    public <T> List<T> queryForList(@NotNull Class<T> clazz, @NotNull String sql, @NotNull Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql),"Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null SQL parameter arguments supplied");
        Preconditions.checkNotNull(clazz, "Null clazz argument supplied");
        try(Connection conn = getConnection(config);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            resolveParameters(stmt, arguments);
            try(ResultSet resultSet = stmt.executeQuery()) {
                List<T> result = new ArrayList<>();
                while(resultSet.next()) {
                    Map<String, Object> dataResult = parseRow(resultSet);
                    result.add(ReflectiveTypeResolver.resolve(clazz, dataResult));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }

    /**
     * Issue a single SQL update operation (such as an insert, update or delete statement)
     * using the supplied SQL statement with the batch of supplied arguments.
     * @author Mario Dennis
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @return number of rows affected
     */
    public int executeUpdate(@NotNull String sql, @Nullable Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        try (Connection conn = getConnection(config);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(arguments != null) {
                resolveParameters(stmt, arguments);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }

    /**
     * Execute a batch update operation (such as an insert, update or delete statement)
     * using the supplied SQL statement with the batch of supplied arguments.
     * @author Mario Dennis
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @return number of rows affected
     */
    public int[] executeBatchUpdate(@NotNull String sql, @NotNull List<Object[]> arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null arguments argument supplied");
        try(Connection conn = getConnection(config);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(arguments.size() == 0) {
                stmt.addBatch();
                return stmt.executeBatch();
            }
            for(Object[] param : arguments) {
                resolveParameters(stmt, param);
                stmt.addBatch();
            }
            return stmt.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e.getMessage());
        }
    }

    /**
     * Used at application shutdown to close all active connection pool dataSources.
     * @author Mario Dennis
     * @throws IOException
     */
    public static void close() throws IOException {
        if(connectionManager == null)
            return;
        connectionManager.close();
    }


    private void resolveParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int x = 0; x < parameters.length; x++) {
            preparedStatement.setObject(x + 1, parameters[x]);
        }
    }

    private Map<String, Object> parseRow(ResultSet resultSet) throws SQLException {
        Map<String, Object> columnMetaData = new HashMap<>();
        int column = resultSet.getMetaData().getColumnCount();
        for (int x = 1; x <= column;x++) {
            String columnName = resultSet.getMetaData().getColumnName(x);
            Object columnValue = resultSet.getObject(x);
            if(columnName != null) {
                columnMetaData.put(columnName.toUpperCase(), columnValue);
            }
        }
        return columnMetaData;
    }

}
