package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionImpl implements Transaction {
    private final Connection connection;
    private final UpdateExecutor updateExecutor;

    public TransactionImpl(Connection connection, UpdateExecutor updateExecutor) {
        Preconditions.checkNotNull(connection);
        Preconditions.checkNotNull(updateExecutor);
        this.updateExecutor = updateExecutor;
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
    public synchronized void commit() {
        try {
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }
}
