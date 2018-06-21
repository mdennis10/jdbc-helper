package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.config.DbConfiguration;
import com.google.common.base.Optional;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public final class C3p0ConnectionManagerImpl implements ConnectionManager {
    private final ComboPooledDataSource dataSource;

    protected C3p0ConnectionManagerImpl(DbConfiguration config) {
        try {
            ComboPooledDataSource c3p0 = new ComboPooledDataSource();
            c3p0.setDriverClass(config.getDriverClassName()); //loads the jdbc driver
            c3p0.setJdbcUrl(config.getUrl());
            c3p0.setUser(config.getUsername());
            c3p0.setPassword(config.getPassword());

            c3p0.setMinPoolSize(config.getMinPoolSize());
            c3p0.setMaxPoolSize(config.getMaxPoolSize());
            dataSource = c3p0;
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Connection> getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection != null ?
                    Optional.of(connection) :
                    Optional.<Connection>absent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDriverClassName() {
        return this.dataSource.getDriverClass();
    }

    @Override
    public String getDatabaseUser() {
        return this.dataSource.getUser();
    }

    @Override
    public String getJbcUrl() {
        return this.dataSource.getJdbcUrl();
    }

    @Override
    public int getMaxPoolSize() {
        return this.dataSource.getMaxPoolSize();
    }

    @Override
    public int getMinPoolSize() {
        return this.dataSource.getMinPoolSize();
    }

    @Override
    public int getMaxStatements() {
        return this.dataSource.getMaxStatements();
    }
}
