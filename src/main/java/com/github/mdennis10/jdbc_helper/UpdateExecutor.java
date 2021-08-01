package com.github.mdennis10.jdbc_helper;

import com.github.mdennis10.jdbc_helper.exception.DatabaseHelperSQLException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateExecutor {
    protected int executeUpdate (boolean isAutoClose, @NotNull Connection connection, @NotNull String sql, @Nullable Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        try {
            if(isAutoClose) {
                try (Connection conn = connection;
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    return execute(stmt, arguments);
                }
            } else {
                PreparedStatement stmt = connection.prepareStatement(sql);
                return execute(stmt, arguments);
            }
        } catch (SQLException e) {
            throw new DatabaseHelperSQLException(e);
        }
    }

    private int execute (PreparedStatement stmt, Object[] arguments) throws SQLException {
        if(arguments != null) {
            ExecutorHelperUtil.resolveParameters(stmt, arguments);
        }
        return stmt.executeUpdate();
    }

}
