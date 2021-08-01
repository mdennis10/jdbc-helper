package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TransactionImpl implements Transaction {
    private final Connection connection;
    private final UpdateExecutor updateExecutor;
    private final QueryExecutor queryExecutor;

    protected TransactionImpl(Connection connection, UpdateExecutor updateExecutor, QueryExecutor queryExecutor) {
        Preconditions.checkNotNull(connection);
        Preconditions.checkNotNull(updateExecutor);
        this.updateExecutor = updateExecutor;
        this.queryExecutor = queryExecutor;
        this.connection = connection;
        disableAutoCommit();
    }

    public synchronized void disableAutoCommit() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    @Override
    public synchronized void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    @Override
    public int executeUpdate(String sql, @Nullable Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        return updateExecutor.executeUpdate(false, connection, sql, arguments);
    }

    @Override
    public int[] executeBatchUpdate(String sql, List<Object[]> arguments) {
        return updateExecutor.executeBatchUpdate(false, connection, sql, arguments);
    }

    @Override
    public <T> Optional<T> query(Class<T> clazz, String sql, @NotNull Object[] arguments) {
        return queryExecutor.query(false, connection, clazz, sql, arguments);
    }

    @Override
    public <T> List<T> queryForList(Class<T> clazz, String sql, @NotNull Object[] arguments) {
        return queryExecutor.queryForList(false, connection, clazz, sql, arguments);
    }

    @Override
    public synchronized void commit() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }
}
