package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.config.DbConfiguration;
import com.dennis.jdbc.helper.exception.HelperSQLException;
import com.dennis.jdbc.helper.exception.InternalHelperException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

public final class C3p0ConnectionManagerImpl implements ConnectionManager {
    private static final Logger LOGGER = Logger.getLogger(C3p0ConnectionManagerImpl.class.getName());
    private final ComboPooledDataSource dataSource;

    C3p0ConnectionManagerImpl(DbConfiguration config) {
        LOGGER.setLevel(Level.WARNING);
        try {
            ComboPooledDataSource c3p0 = new ComboPooledDataSource();
            c3p0.setDriverClass(config.getDriverClassName()); //loads the jdbc driver
            c3p0.setJdbcUrl(config.getUrl());
            c3p0.setUser(config.getUsername());
            c3p0.setPassword(config.getPassword());

            c3p0.setMinPoolSize(config.getMinPoolSize());
            c3p0.setMaxPoolSize(config.getMaxPoolSize());
            c3p0.setInitialPoolSize(config.getMinPoolSize());
            dataSource = c3p0;
        } catch (PropertyVetoException e) {
            LOGGER.severe(e.getMessage());
            throw new InternalHelperException(e);
        }
    }

    @Override
    public Optional<Connection> getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            return connection != null ?
                    Optional.of(connection) :
                    Optional.empty();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new HelperSQLException(e);
        }
    }

    @Override
    public void closeConnections() {
        dataSource.close();
    }

    @Override
    public synchronized String getDriverClassName() {
        return this.dataSource.getDriverClass();
    }

    @Override
    public synchronized String getDatabaseUser() {
        return this.dataSource.getUser();
    }

    @Override
    public synchronized String getJbcUrl() {
        return this.dataSource.getJdbcUrl();
    }

    @Override
    public synchronized int getMaxPoolSize() {
        return this.dataSource.getMaxPoolSize();
    }

    @Override
    public synchronized int getMinPoolSize() {
        return this.dataSource.getMinPoolSize();
    }

    @Override
    public synchronized int getMaxStatements() {
        return this.dataSource.getMaxStatements();
    }
}
