package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.exception.ConnectionException;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.sql.Connection;
import java.util.List;

public final class DatabaseHelper extends AbstractDatabaseHelper {
    private final String profile;
    private final SQLExecutor executor;

    private DatabaseHelper(String profile) {
        this.profile = profile;
        this.executor = new SQLExecutor();
    }

    /**
     * Get new instance of DatabaseHelper using the
     * default connection settings profile.
     * @author Mario Dennis
     * @return DatabaseHelper instance
     */
    public static DatabaseHelper getInstance() {
        return getInstance("default");
    }

    /**
     * Get new instance of Database using the
     * define connection settings profile.
     * @author Mario Dennis
     * @param connectionProfile
     * @return DatabaseHelper instance
     */
    public static DatabaseHelper getInstance(String connectionProfile) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(connectionProfile));
        return new DatabaseHelper(connectionProfile);
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
        return !result.isEmpty() ? Optional.of(result.get(0)) : Optional.<T>absent();
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

        Optional<Connection> connection;
        connection = ConnectionManagerFactory
            .getConnectionManager(profile)
            .getConnection();

        if (!connection.isPresent())
            throw new ConnectionException();
        ExecutionResult result = null;
        List<T> data;
        try {
            result = executor.execute(connection.get(), sql, params);
            data = parseEntity(result.getResultSet(), clazz);
        } finally {
            close(result);
        }
        return data;
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
        Optional<Connection> connection;
        connection = ConnectionManagerFactory
            .getConnectionManager(profile)
            .getConnection();

        if (!connection.isPresent())
            throw new ConnectionException();
        UpdateExecutionResult result = null;
        try {
            result = executor.executeUpdate(connection.get(), sql, params);
        } finally {
            close(result);
        }
        return result != null ? result.getRowsAffected() : 0;
    }

    public void cleanUp() {
        ConnectionManagerFactory.closeConnectionManagers();
    }

    /**
     * Get the maximum connection the connection pool
     * will retain.
     * @author Mario Dennis
     * @return connection pool max size
     */
    public final int getMaxConnectionPoolSize() {
        return ConnectionManagerFactory
                .getConnectionManager(profile)
                .getMaxPoolSize();
    }

    /**
     * Get minimum connection the connection pool
     * contains at any given time.
     * @author Mario Dennis
     * @return connection pool min size
     */
    public final int getMinConnectionPoolSize() {
        return ConnectionManagerFactory
                .getConnectionManager(profile)
                .getMinPoolSize();
    }

    /**
     * Get database username
     * @author Mario Dennis
     * @return username
     */
    public final String getDatabaseUser() {
        return ConnectionManagerFactory
                .getConnectionManager(profile)
                .getDatabaseUser();
    }

    /**
     * Get jdbc database url
     * @author Mario Dennis
     * @return jdbc url
     */
    public String getJdbcUrl() {
        return ConnectionManagerFactory
                .getConnectionManager(profile)
                .getJbcUrl();
    }

    /**
     * Get jdbc class driver name
     * @author Mario Dennis
     * @return driver class name
     */
    public String getDriverClassName() {
        return ConnectionManagerFactory
                .getConnectionManager(profile)
                .getDriverClassName();
    }
}