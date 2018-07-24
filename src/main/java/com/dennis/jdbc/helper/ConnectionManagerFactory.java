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
            connectionMap.put(profile, createConnectionManager(config));
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

    public static void closeConnectionManagers() {
        if(connectionMap == null || connectionMap.isEmpty()) {
            return;
        }
        connectionMap.entrySet().forEach(con ->
            con.getValue().close()
        );
    }

    public static void closeConnectionManager(String profile) {
        if(connectionMap == null || connectionMap.isEmpty()) {
            return;
        }
        ConnectionManager connectionManager = connectionMap.get(profile);
        if(connectionManager != null) connectionManager.close();
    }

    private ConnectionManager createConnectionManager(DbConfiguration config) {
        return new C3p0ConnectionManagerImpl(config);
    }
}
