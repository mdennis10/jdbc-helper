package com.dennis.jdbc.extension.core.config;

import com.dennis.jdbc.extension.core.exception.NameConfigNotFoundException;
import com.dennis.jdbc.extension.core.util.RefStreamsUtil;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;


public class PropertyFileLoader {
    protected String filename = "jdbc-application.properties";

    protected Optional<Properties> load() {
        InputStream input = getClass().getClassLoader().getResourceAsStream(filename);
        try {
            Properties prop = new Properties();
            prop.load(input);
            if (prop == null || prop.size() <= 0) {
                return Optional.absent();
            }
            return Optional.of(prop);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return Optional.absent();
        } finally {
            close(input);
        }
        return Optional.absent();
    }

    private void close(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected Optional<DbConfiguration> getDbConfiguration(String profile, Properties prop) {
        if (prop == null || prop.size() <= 0) {
            return Optional.absent();
        }

        if (profile.toLowerCase().equals("default")) {
            DbConfiguration config = new DbConfiguration(
                    prop.getProperty("jdbc.username"),
                    prop.getProperty("jdbc.password", ""),
                    prop.getProperty("jdbc.url"),
                    prop.getProperty("jdbc.driver"),
                    parseIntPropertyValue(prop,"jdbc.pool.max-size", 15),
                    parseIntPropertyValue(prop,"jdbc.pool.min-size", 8)
            );
            return Optional.of(config);
        } else {
            if (!getProfiles(prop).contains(profile)) {
                throw new NameConfigNotFoundException(profile);
            }
            DbConfiguration config = new DbConfiguration(
                    prop.getProperty(String.format("%s.jdbc.username", profile)),
                    prop.getProperty(String.format("%s.jdbc.password", profile), ""),
                    prop.getProperty(String.format("%s.jdbc.url", profile)),
                    prop.getProperty(String.format("%s.jdbc.driver", profile)),
                    parseIntPropertyValue(prop, String.format("%s.jdbc.pool.max-size", profile), 15),
                    parseIntPropertyValue(prop, String.format("%s.jdbc.pool.min-size", profile), 8)
            );
            return Optional.of(config);
        }
    }

    private int parseIntPropertyValue(Properties prop, String property, int defaultValue) {
        try {
            if(prop == null || prop.size() == 0)
                return defaultValue;
            String value = prop.getProperty(property, Integer.toString(defaultValue));
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw e;
        }
    }
    protected Set<String> getProfiles(Properties prop) {
        Set<String> propNames = prop.stringPropertyNames();
        final Set<String> profile = new HashSet<String>();
        RefStreamsUtil.createStream(propNames).forEach(name -> {
            List<String> result = Lists.newArrayList(Splitter.on("jdbc")
                    .trimResults()
                    .split(name)
            );
            String key = result.get(0);
            if (!Strings.isNullOrEmpty(key)) {
                if (key.charAt(key.length() - 1) == '.') {
                    key = key.substring(0, key.length() - 1);
                }
                if (key.charAt(0) == '.') {
                    key = key.substring(1, key.length());
                }
                profile.add(key);
            }
        });
        profile.add("default");
        return profile;
    }
}
