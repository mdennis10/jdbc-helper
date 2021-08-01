package com.github.mdennis10.jdbc_helper;


import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class DatabaseHelper {
    private static ConnectionManager connectionManager;
    private final DbConfig config;
    private final UpdateExecutor updateExecutor;
    private final QueryExecutor queryExecutor;

    public DatabaseHelper(DbConfig config) {
        this.config = config;
        this.connectionManager = HikariConnectionManager.getInstance();
        this.updateExecutor = new UpdateExecutor();
        this.queryExecutor = new QueryExecutor();
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
        return queryExecutor.query(true, getConnection(config), sql, arguments, mapper);
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
        return queryExecutor.query(true, getConnection(config), clazz, sql, arguments);
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
        return queryExecutor.queryForList(true, getConnection(config), sql, arguments, mapper);
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
        return queryExecutor.queryForList(true, getConnection(config), clazz, sql, arguments);
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
        return updateExecutor.executeUpdate(true, getConnection(config), sql, arguments);
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
        return updateExecutor.executeBatchUpdate(true, getConnection(config), sql, arguments);
    }

    /**
     * Get transaction instance used for executing transactional
     * operations
     * @author Mario Dennis
     * @return Transaction
     */
    public Transaction getTransaction() {
        return new TransactionImpl(getConnection(config), updateExecutor, queryExecutor);
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
