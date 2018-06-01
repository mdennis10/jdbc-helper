package com.dennis.jdbc.extension.core;

import com.google.common.base.Optional;

import java.sql.Connection;

public interface ConnectionManager {
    Optional<Connection> getConnection();

    String getDriverClassName();

    String getDatabaseUser();

    String getJbcUrl();

    int getMaxPoolSize();

    int getMaxStatements();
}
