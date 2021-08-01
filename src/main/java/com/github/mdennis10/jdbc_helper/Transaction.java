package com.github.mdennis10.jdbc_helper;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface Transaction {
    /**
     * Issue a single SQL update operation (such as an insert, update or delete statement)
     * using the supplied SQL statement with the batch of supplied arguments.
     * @author Mario Dennis
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @return number of rows affected
     */
    int executeUpdate(@NotNull String sql, @Nullable Object[] arguments);

    /**
     * Query database using given SQL data access statement provided.
     * @author Mario Dennis
     * @param clazz - entity class type
     * @param sql - the SQL query to execute
     * @param arguments - arguments to bind to the query
     * @param <T> - entity class
     * @return instance of entity class with result row mapped
     */
    <T> Optional<T> query(@NotNull Class<T> clazz, @NotNull String sql, @NotNull Object[] arguments);

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
