package com.github.mdennis10.jdbc_helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;

public final class HikariConnectionManager implements ConnectionManager {
    private static final ConcurrentMap<DbConfig, HikariDataSource> dataSources = new ConcurrentHashMap<>();
    private static final HikariConnectionManager INSTANCE = new HikariConnectionManager();
    private static final long MAX_LIFE_TIME = MINUTES.toMillis(4);
    private static final int MIN_IDLE = 0;
    private static final long IDLE_TIMEOUT = MINUTES.toMillis(4);
    private static final int MAX_POOL_SIZE = 30;
    private HikariConnectionManager() { }

    public static HikariConnectionManager getInstance() {
        return INSTANCE;
    }

    private static HikariDataSource createDataSource(DbConfig config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setMaxLifetime(MAX_LIFE_TIME);
        hikariConfig.setIdleTimeout(IDLE_TIMEOUT);
        hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
        hikariConfig.setMinimumIdle(MIN_IDLE);
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
