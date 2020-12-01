package com.github.mdennis10.jdbc_helper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HikariConnectionManagerTest {
    private static final DbConfig config = new DbConfig(
            "sa",
            "pass@4d1",
            "jdbc:h2:file:~/helper_test",
            "org.h2.Driver"
    );

    private static final DbConfig config2 = new DbConfig(
            "usra",
            "pass@4d1",
            "jdbc:h2:file:~/helper_test1",
            "org.h2.Driver"
    );

    @AfterAll static void tearDown() {
        HikariConnectionManager
                .getInstance()
                .getAllDataSources()
                .forEach(x -> x.close());
    }

    @Test void getDataSource_dataSourceLookUpByDbConfigTest() {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        //assert that the same dataSource is return once the correct DbConfig is provided
        assertEquals(
            connectionManager.getDataSource(config).getPoolName(),
            connectionManager.getDataSource(config).getPoolName()
        );

        // assert that the appropriate dataSource is return give the correct DbConfig
        assertNotEquals(
            connectionManager.getDataSource(config).getPoolName(),
            connectionManager.getDataSource(config2).getPoolName()
        );
    }

    @Test void getDataSource() {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        HikariDataSource dataSource = connectionManager.getDataSource(config);
        assertNotNull(dataSource);
        assertFalse(dataSource.isClosed());
    }

    @Test void getDataSource_recreateClosedDataSourcesTest() {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        HikariDataSource dataSource = connectionManager.getDataSource(config);
        dataSource.close();
        assertFalse(connectionManager.getDataSource(config).isClosed());
        assertNotEquals(dataSource.getPoolName(), connectionManager.getDataSource(config).getPoolName());
    }

    @Test void close () throws IOException {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        // initialize two dataSource
        connectionManager.getDataSource(config);
        connectionManager.getDataSource(config2);

        connectionManager.close(); // close all dataSources
        List<HikariDataSource> dataSources = connectionManager.getAllDataSources();
        assertNotNull(dataSources);
        assertEquals(2, dataSources.size());
        assertTrue(dataSources.stream().allMatch(dataSource -> dataSource.isClosed()));
    }

    //@Test
    void validateDataSourceAutomaticClosesTest() throws InterruptedException {
        final long TIMEOUT = TimeUnit.SECONDS.toMillis(20);
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.getUrl());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setDriverClassName(config.getDriverClassName());
        hikariConfig.setMaxLifetime(TIMEOUT);
        hikariConfig.setIdleTimeout(TIMEOUT);
        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMinimumIdle(0);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        Thread.sleep(TimeUnit.SECONDS.toMillis(60));
        assertTrue(dataSource.isClosed());
    }
}