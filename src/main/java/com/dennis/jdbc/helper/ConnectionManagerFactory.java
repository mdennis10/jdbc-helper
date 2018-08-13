package com.dennis.jdbc.helper;

import com.dennis.jdbc.helper.config.DbConfiguration;
import com.dennis.jdbc.helper.config.DbConfigurationFactory;
import com.dennis.jdbc.helper.exception.NameConfigNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConnectionManagerFactory {
    private static Map<String, ConnectionManager> connectionMap;
    private static final Logger LOGGER = Logger.getLogger(ConnectionManagerFactory.class.getName());

    protected ConnectionManagerFactory() {
        LOGGER.setLevel(Level.WARNING);
        this.connectionMap = new HashMap<>();
        synchronized (connectionMap) {
            Set<String> profiles = DbConfigurationFactory.getProfiles();
            profiles.forEach(profile -> {
                DbConfiguration config = DbConfigurationFactory.getDbConfiguration(profile);
                if (config == null) return;
                connectionMap.put(profile, createConnectionManager(config));
            });
        }
    }

    protected static ConnectionManager getConnectionManager(String profile) {
        if (connectionMap == null) {
            new ConnectionManagerFactory();
        }
        ConnectionManager connectionManager = connectionMap.get(profile);
        if (connectionManager == null) {
            NameConfigNotFoundException exception = new NameConfigNotFoundException(profile);
            LOGGER.severe(exception.getMessage());
            throw exception;
        }
        return connectionManager;
    }

    public synchronized static void closeConnectionManagers() {
        if(connectionMap == null || connectionMap.isEmpty()) {
            return;
        }
        connectionMap.entrySet().forEach(con ->
            con.getValue().closeConnections()
        );
    }

    public synchronized static void closeConnectionManager(String profile) {
        if(connectionMap == null || connectionMap.isEmpty()) {
            return;
        }
        ConnectionManager connectionManager = connectionMap.get(profile);
        if(connectionManager != null) connectionManager.closeConnections();
    }

    private ConnectionManager createConnectionManager(DbConfiguration config) {
        return new C3p0ConnectionManagerImpl(config);
    }
}
