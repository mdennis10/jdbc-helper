package com.jdbc.helper;

import java.util.Objects;

public class DbConfig {
    private final String user;
    private final String password;
    private final String url;
    private final String driverClassName;
    private int maxPoolSize = 30;
    private int minPoolSize = 3;

    public DbConfig(String user, String password, String url, String driverClassName) {
        this.user = user;
        this.password = password;
        this.url = url;
        this.driverClassName = driverClassName;
    }

    public DbConfig(String user, String password, String url, String driverClassName, int maxPoolSize, int minPoolSize) {
        this.user = user;
        this.password = password;
        this.url = url;
        this.driverClassName = driverClassName;
        this.maxPoolSize = maxPoolSize;
        this.minPoolSize = minPoolSize;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbConfig dbConfig = (DbConfig) o;
        return getMaxPoolSize() == dbConfig.getMaxPoolSize() &&
                getMinPoolSize() == dbConfig.getMinPoolSize() &&
                getUser().equals(dbConfig.getUser()) &&
                getPassword().equals(dbConfig.getPassword()) &&
                getUrl().equals(dbConfig.getUrl()) &&
                getDriverClassName().equals(dbConfig.getDriverClassName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getPassword(), getUrl(), getDriverClassName(), getMaxPoolSize(), getMinPoolSize());
    }
}
