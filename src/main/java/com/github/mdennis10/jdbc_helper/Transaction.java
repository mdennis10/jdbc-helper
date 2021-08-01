package com.github.mdennis10.jdbc_helper;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

public interface Transaction {
    /**
     * Execute update sql statements
     * @athor Mario Dennis
     * @param sql
     * @param arguments
     */
    int executeUpdate(@NotNull String sql, @Nullable Object[] arguments);

    /**
     * Rollback transaction
     * @author Mario Dennis
     */
    void rollback();

    /**
     * Commits transaction and closes connection
     * @author Mario Dennis
     */
    void commit();
}
