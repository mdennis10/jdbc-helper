package com.jdbc.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    void getDataSource_sameDataSourceReturnWhenUrlIsTheSame() {
        assertEquals(
            HikariConnectionManager.getInstance(config).getDataSource(config).getPoolName(),
            HikariConnectionManager.getInstance(config).getDataSource(config).getPoolName()
        );
        assertNotEquals(
            HikariConnectionManager.getInstance(config).getDataSource(config).getPoolName(),
            HikariConnectionManager.getInstance(config2).getDataSource(config2).getPoolName()
        );
    }
}