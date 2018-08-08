package com.dennis.jdbc.helper.config;

import com.dennis.jdbc.helper.exception.ConfigurationException;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbConfigurationFactory {
    private static final String DEFAULT = "default";
    private static Map<String, DbConfiguration> configurationMap;
    private static final Logger LOGGER = Logger.getLogger(DbConfigurationFactory.class.getName());

    private DbConfigurationFactory() {
        LOGGER.setLevel(Level.WARNING);
        this.configurationMap = new HashMap<>();
        final PropertyFileLoader loader = new PropertyFileLoader();
        final Optional<Properties> prop = loader.load();
        if (!prop.isPresent()) {
            ConfigurationException exception = new ConfigurationException("unable to load configuration properties file");
            LOGGER.severe(exception.getMessage());
            throw exception;
        }
        Set<String> profiles = loader.getProfiles(prop.get());
        profiles.forEach(profile -> {
            Optional<DbConfiguration> config = loader.getDbConfiguration(profile, prop.get());
            if (!config.isPresent()) return;
            configurationMap.put(profile, config.get());
        });
    }

    public static Set<String> getProfiles() {
        if (configurationMap == null) {
            new DbConfigurationFactory();
        }
        return configurationMap.keySet();
    }

    /**
     * Default DbConfiguration instance
     *
     * @return DbConfiguration
     * @author Mario Dennis
     */
    public static DbConfiguration getDbConfiguration() {
        return getDbConfiguration(DEFAULT);
    }

    /**
     * Default DbConfiguration instance
     *
     * @return DbConfiguration
     * @author Mario Dennis
     */
    public static DbConfiguration getDbConfiguration(String profile) {
        if (configurationMap == null) {
            new DbConfigurationFactory();
        }
        return configurationMap.get(profile);
    }
}
