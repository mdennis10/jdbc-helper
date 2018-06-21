package com.dennis.jdbc.helper.config;

import com.dennis.jdbc.helper.exception.ConfigurationException;
import com.dennis.jdbc.helper.util.RefStreamsUtil;
import com.google.common.base.Optional;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


public class DbConfigurationFactory {
    private static final String DEFAULT = "default";
    private static Map<String, DbConfiguration> configurationMap;

    private DbConfigurationFactory() {
        this.configurationMap = new HashMap<String, DbConfiguration>();
        final PropertyFileLoader loader = new PropertyFileLoader();
        final Optional<Properties> prop = loader.load();
        if (!prop.isPresent()) {
            throw new ConfigurationException("unable to load configuration properties file");
        }
        Set<String> profiles = loader.getProfiles(prop.get());
        RefStreamsUtil.createStream(profiles).forEach(profile -> {
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
        if (configurationMap == null) {
            new DbConfigurationFactory();
        }
        return getDbConfiguration(DEFAULT);
    }

    /**
     * Default DbConfiguration instance
     *
     * @return DbConfiguration
     * @author Mario Dennis
     */
    public static DbConfiguration getDbConfiguration(String profile) {
        return configurationMap.get(profile);
    }
}
