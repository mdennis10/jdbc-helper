package com.dennis.jdbc.extension.core;

import com.google.common.base.Optional;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class C3p0ConnectionManagerImpl implements ConnectionManager {
    private ComboPooledDataSource dataSource;

    protected C3p0ConnectionManagerImpl(String url, String username, String password, String driverClassName) {
        try {
            ComboPooledDataSource c3p0 = new ComboPooledDataSource();
            c3p0.setDriverClass(driverClassName); //loads the jdbc driver
            c3p0.setJdbcUrl(url);
            c3p0.setUser(username);
            c3p0.setPassword(password);

            c3p0.setMinPoolSize(5);
            c3p0.setMaxPoolSize(15);
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
    public int getMaxStatements() {
        return this.dataSource.getMaxStatements();
    }
}
