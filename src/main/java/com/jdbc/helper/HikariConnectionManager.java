package com.jdbc.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HikariConnectionManager implements ConnectionManager {
    private static final ConcurrentMap<DbConfig, HikariDataSource> dataSources = new ConcurrentHashMap<>();
    private static final HikariConnectionManager INSTANCE = new HikariConnectionManager();
    private HikariConnectionManager() { }

    public static HikariConnectionManager getInstance(DbConfig config) {
        if(!dataSources.containsKey(config)) {
            dataSources.putIfAbsent(config, createDataSource(config));
        }
        return INSTANCE;
    }

    private static HikariDataSource createDataSource(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        return new HikariDataSource(hikariConfig);
    }

    @Override
    public HikariDataSource getDataSource(DbConfig config) {
        return dataSources.get(config);
    }
}
