package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.config.DbConfiguration;
import com.dennis.jdbc.helper.config.DbConfigurationFactory;
import com.dennis.jdbc.helper.exception.NameConfigNotFoundException;
import com.dennis.jdbc.helper.util.RefStreamsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ConnectionManagerFactory {
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
        return new C3p0ConnectionManagerImpl(config);
    }
}
