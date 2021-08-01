package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UpdateExecutor {
    protected int executeUpdate (boolean isAutoClose, @NotNull Connection connection, @NotNull String sql, @Nullable Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        try {
            if(isAutoClose) {
                try (Connection conn = connection;
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return executeUpdate(stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeUpdate(stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    private int executeUpdate(PreparedStatement stmt, Object[] arguments) throws SQLException {
        if(arguments != null) {
            ExecutorHelperUtil.resolveParameters(stmt, arguments);
        }
        return stmt.executeUpdate();
    }

    protected int[] executeBatchUpdate(boolean isAutoClose, @NotNull Connection connection, @NotNull String sql, @NotNull List<Object[]> arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        Preconditions.checkNotNull(arguments, "Null arguments argument supplied");
        try {
            if(isAutoClose) {
                try(Connection conn = connection;
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return executeBatchUpdate(stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return executeBatchUpdate(stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    private int[] executeBatchUpdate(PreparedStatement stmt, List<Object[]> arguments) throws SQLException  {
        if(arguments.size() == 0) {
            stmt.addBatch();
            return stmt.executeBatch();
        }
        for(Object[] param : arguments) {
            ExecutorHelperUtil.resolveParameters(stmt, param);
            stmt.addBatch();
        }
        return stmt.executeBatch();
    }
}
