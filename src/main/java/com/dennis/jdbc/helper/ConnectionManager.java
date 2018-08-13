package com.dennis.jdbc.helper;

import java.sql.Connection;
import java.util.Optional;

public interface ConnectionManager {
    Optional<Connection> getConnection();

    String getDriverClassName();

    String getDatabaseUser();

    String getJbcUrl();

    int getMaxPoolSize();

    int getMinPoolSize();

    int getMaxStatements();

    void closeConnections();
}
