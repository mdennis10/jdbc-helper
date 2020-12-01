package com.github.mdennis10.jdbc_helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DbConfigTest {
    private static final DbConfig config = new DbConfig(
            "sa",
            "pass@4d1",
            "jdbc:h2:file:~/helper_test",
            "org.h2.Driver"
    );

    @Test void constructorParametersTest() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new DbConfig(null,config.getPassword(),config.getUrl(),config.getDriverClassName())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DbConfig("",config.getPassword(),config.getUrl(),config.getDriverClassName())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new DbConfig(config.getPassword(), config.getPassword(),null,config.getDriverClassName())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new DbConfig(config.getPassword(), config.getPassword(),"",config.getDriverClassName())
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new DbConfig(config.getPassword(), config.getPassword(),config.getUrl(),null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new DbConfig(config.getPassword(), config.getPassword(),config.getUrl(),"")
        );
    }
}