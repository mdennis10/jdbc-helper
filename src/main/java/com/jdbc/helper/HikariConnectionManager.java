package com.jdbc.helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class HikariConnectionManager implements ConnectionManager {
    private static final ConcurrentMap<DbConfig, HikariDataSource> dataSources = new ConcurrentHashMap<>();
    private static final HikariConnectionManager INSTANCE = new HikariConnectionManager();
    private static final int MAX_LIFE_TIME = 4;
    private static final long IDLE_TIMEOUT = 4;
    private HikariConnectionManager() { }

    public static HikariConnectionManager getInstance() {
        return INSTANCE;
    }

    private static HikariDataSource createDataSource(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setMaxLifetime(MAX_LIFE_TIME);
        hikariConfig.setIdleTimeout(IDLE_TIMEOUT);
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setMaximumPoolSize(config.getMaxPoolSize());
        hikariConfig.setMinimumIdle(config.getMinPoolSize());
        return new HikariDataSource(hikariConfig);
    }

    protected List<HikariDataSource> getAllDataSources() {
        return dataSources.values().stream().collect(Collectors.toList());
    }

    @Override
    public HikariDataSource getDataSource(DbConfig config) {
        if(!dataSources.containsKey(config)) {
            dataSources.putIfAbsent(config, createDataSource(config));
        } else if (dataSources.get(config).isClosed()) {
            dataSources.remove(config);
            return getDataSource(config);
        }
        return dataSources.get(config);
    }

    @Override
    public void close() throws IOException {
        dataSources.entrySet().stream().forEach(x -> x.getValue().close());
    }

}
