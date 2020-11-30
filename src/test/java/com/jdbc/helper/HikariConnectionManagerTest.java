package com.jdbc.helper;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

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

    @Test void getDataSource_dataSourceLookUpByDbConfig() {
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

    @Test void getDataSource_recreateDataSource() {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        HikariDataSource dataSource = connectionManager.getDataSource(config);
        dataSource.close();
        assertFalse(connectionManager.getDataSource(config).isClosed());
        assertNotEquals(dataSource.getPoolName(), connectionManager.getDataSource(config).getPoolName());
    }

    @Test void close () throws IOException {
        HikariConnectionManager connectionManager = HikariConnectionManager.getInstance();
        HikariConnectionManager.getInstance();// initialize second dataSource

        connectionManager.close();
        List<HikariDataSource> dataSources = connectionManager.getAllDataSources();
        assertNotNull(dataSources);
        assertEquals(2, dataSources.size());
        assertTrue(dataSources.stream().allMatch(dataSource -> dataSource.isClosed()));
    }
}