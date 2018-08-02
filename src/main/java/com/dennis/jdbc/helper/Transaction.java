package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.HelperSqlException;
import com.dennis.jdbc.helper.util.ConnectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Transaction extends AbstractDatabaseHelper {
    private final SQLExecutor executor;
    private final Connection connection;

    private Transaction(String profile) {
        this.executor = new SQLExecutor();
        this.connection = ConnectionManagerFactory
            .getConnectionManager(profile)
            .getConnection().get();
        setAutoCommit(connection, false);
    }

    public static Transaction of(String profile) {
        return new Transaction(profile);
    }

    public static Transaction get() {
        return new Transaction("default");
    }

    /**
     * Execute a read operation sql query that
     * return a single row.
     * @author Mario Dennis
     * @param clazz - entity class type for result set parsing
     * @param sql - sql query to execute
     * @param params - parameters used in sql query
     * @return DatabaseHelper instance
     */
    public <T> Optional<T> executeQuery(Class<T> clazz, String sql, Object... params) {
        List<T> result = executeQueryCollection(clazz, sql, params);
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.absent();
    }

    /**
     * Execute a read operation sql query that
     * return a multiple rows.
     * @author Mario Dennis
     * @param clazz - entity class type for result set parsing
     * @param sql - sql query to execute
     * @param params - parameters used in sql query
     * @return DatabaseHelper instance
     */
    public <T> List<T> executeQueryCollection(Class<T> clazz, String sql, Object... params) {
        Preconditions.checkArgument(clazz != null, "clazz type not supplied");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql));

        ExecutionResult executionResult = null;
        List<T> result;
        try {
            executionResult = executor.execute(connection, sql, params);
            result = parseEntity(executionResult.getResultSet(), clazz);
        } finally {
            close(executionResult);
        }
        return result;
    }

    /**
     * Execute query that modifies rows.
     * @author Mario Dennis
     * @param sql - sql query to execute
     * @param params - parameters used in sql query
     * @return rows affected
     */
    public int executeUpdateQuery(String sql, Object... params) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql));
        UpdateExecutionResult result = null;
        try {
            result = executor.executeUpdate(connection, sql, params);
        } finally {
            close(result);
        }
        return result != null ? result.getRowsAffected() : 0;
    }

    private void setAutoCommit(Connection connection, boolean isAutoCommit) {
        if(ConnectionUtil.isClosed(connection))
            return;
        try {
            connection.setAutoCommit(isAutoCommit);
        } catch (SQLException e) {
            throw new HelperSqlException(e);
        }
    }

    public void rollback() {
        if(ConnectionUtil.isClosed(connection))
            return;
        try {
            this.connection.rollback();
        } catch (SQLException e){
            throw new HelperSqlException(e);
        }
    }

    public void commit() {
        if(ConnectionUtil.isClosed(connection))
            return;
        try {
            this.connection.commit();
        } catch(SQLException e) {
            throw new HelperSqlException(e);
        }
    }

    public void close() {
        ConnectionUtil.close(connection);
    }

    @Override
    protected void close(ExecutionResult executionResult) {
        if (executionResult == null)
            return;
        try {
            if (executionResult.getPreparedStatement() != null)
                executionResult.getPreparedStatement().close();
            if (executionResult.getResultSet() != null)
                executionResult.getResultSet().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
