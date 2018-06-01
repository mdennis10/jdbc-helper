package com.dennis.jdbc.extension.core;

import com.dennis.jdbc.extension.core.config.DbConfiguration;
import com.dennis.jdbc.extension.core.config.DbConfigurationFactory;
import com.dennis.jdbc.extension.core.exception.NameConfigNotFoundException;
import com.dennis.jdbc.extension.core.util.RefStreamsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConnectionManagerFactory {
    private static Map<String, ConnectionManager> connectionMap;

    protected ConnectionManagerFactory() {
        this.connectionMap = new HashMap<String, ConnectionManager>();
        Set<String> profiles = DbConfigurationFactory.getProfiles();
        RefStreamsUtil.createStream(profiles).forEach(profile -> {
            DbConfiguration config = DbConfigurationFactory.getDbConfiguration(profile);
            if (config == null) return;
            connectionMap.put(profile, ConnectionManagerFactory.this.createConnectionManager(config));
        });
    }

    protected static ConnectionManager getConnectionManager(String profile) {
        if (connectionMap == null) {
            new ConnectionManagerFactory();
        }
        ConnectionManager connectionManager = connectionMap.get(profile);
        if (connectionManager == null) {
            throw new NameConfigNotFoundException(profile);
        }
        return connectionManager;
    }

    private ConnectionManager createConnectionManager(DbConfiguration config) {
        return new C3p0ConnectionManagerImpl(
                config.getUrl(),
                config.getUsername(),
                config.getPassword(),
                config.getDriverClassName()
        );
    }
}
