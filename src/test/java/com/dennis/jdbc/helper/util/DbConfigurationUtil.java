package com.dennis.jdbc.helper.util;

import com.dennis.jdbc.helper.config.DbConfiguration;
import com.dennis.jdbc.helper.config.DbConfigurationFactory;
import com.dennis.jdbc.helper.config.PropertyFileLoader;
import com.google.common.base.Optional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DbConfigurationUtil {
    private DbConfigurationUtil() {
    }

    public static DbConfiguration getTestConfig() {
        return new DbConfiguration(
                "sa",
                "",
                "jdbc:h2:file:~/test",
                "org.h2.Driver",
                15,
                5
        );
    }

    public static DbConfiguration mockDbConfig(String url, String username, String password, String className) {
        DbConfiguration configuration = mock(DbConfiguration.class);
        when(configuration.getDriverClassName()).thenReturn(className);
        when(configuration.getPassword()).thenReturn(password);
        when(configuration.getUrl()).thenReturn(url);
        when(configuration.getUsername()).thenReturn(username);
        return configuration;
    }

    public static Optional<Connection> getTestConnection() {
        return getTestConnection("default");
    }

    public static Optional<Connection> getTestConnection(String profile) {
        DbConfiguration config = DbConfigurationFactory.getDbConfiguration(profile);
        try {
            Class.forName(config.getDriverClassName());
            Connection connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );
            return connection != null ?
                    Optional.of(connection) :
                    Optional.<Connection>absent();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
