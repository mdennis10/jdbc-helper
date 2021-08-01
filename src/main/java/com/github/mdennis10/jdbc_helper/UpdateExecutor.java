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
    protected int executeUpdate(boolean isAutoClose, @NotNull Connection connection, @NotNull String sql, @Nullable Object[] arguments) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "Null or empty sql argument supplied");
        if(isAutoClose) {
            try (Connection conn = connection;
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                if(arguments != null) {
                    resolveParameters(stmt, arguments);
                }
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseHelperSQLException(e);
            }
        } else {
            try {
                Connection conn = connection;
                PreparedStatement stmt = conn.prepareStatement(sql);
                if(arguments != null) {
                    resolveParameters(stmt, arguments);
                }
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseHelperSQLException(e);
            }
        }
    }

    private void resolveParameters(PreparedStatement preparedStatement, Object[] parameters) throws SQLException {
        for (int x = 0; x < parameters.length; x++) {
            preparedStatement.setObject(x + 1, parameters[x]);
        }
    }
}
