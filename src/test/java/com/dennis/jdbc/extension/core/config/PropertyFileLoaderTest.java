package com.dennis.jdbc.extension.core.config;

import com.dennis.jdbc.extension.core.exception.NameConfigNotFoundException;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.junit.Test;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PropertyFileLoaderTest {
    @Test
    public void getProfileTest() {
        PropertyFileLoader loader = new PropertyFileLoader();
        Optional<Properties> prop = loader.load();
        Set<String> profiles = loader.getProfiles(prop.get());
        assertFalse(profiles.isEmpty());
        assertTrue(profiles.contains("default"));
        assertTrue(profiles.contains("myprofile"));
    }

    @Test
    public void loadTest() {
        PropertyFileLoader loader = new PropertyFileLoader();
        Optional<Properties> config = loader.load();
        assertNotNull(config);
        assertTrue(config.isPresent());
        assertTrue(config.get().stringPropertyNames().size() > 0);
    }

    @Test
    public void noApplicationConfigFileFound() {
        PropertyFileLoader loader = new PropertyFileLoader();
        loader.filename = "some-file-that-do-not-exist.properties";
        Optional<Properties> config = loader.load();
        assertFalse(config.isPresent());
    }

    @Test
    public void getDbConfiguration_DefaultProfileTest() {
        PropertyFileLoader loader = new PropertyFileLoader();
        Properties properties = mock(Properties.class);
        when(properties.getProperty("jdbc.url")).thenReturn("jdbc:h2:file:~/test");
        when(properties.getProperty("jdbc.username")).thenReturn("sa");
        when(properties.getProperty("jdbc.driver")).thenReturn("org.h2.Driver");
        when(properties.getProperty("jdbc.password", "")).thenReturn("some-password");
        when(properties.size()).thenReturn(4);

        Optional<DbConfiguration> config = loader.getDbConfiguration("default", properties);
        assertTrue(config.isPresent());
        assertFalse(Strings.isNullOrEmpty(config.get().getDriverClassName()));
        assertFalse(Strings.isNullOrEmpty(config.get().getUsername()));
        assertFalse(Strings.isNullOrEmpty(config.get().getUrl()));
        assertFalse(Strings.isNullOrEmpty(config.get().getPassword()));
    }

    @Test
    public void getDbConfiguration_ProfileTest() {
        PropertyFileLoader loader = new PropertyFileLoader();
        Properties properties = mock(Properties.class);
        when(properties.getProperty("myprofile.jdbc.url")).thenReturn("jdbc:h2:file:~/test");
        when(properties.getProperty("myprofile.jdbc.username")).thenReturn("sa");
        when(properties.getProperty("myprofile.jdbc.driver")).thenReturn("org.h2.Driver");
        when(properties.getProperty("myprofile.jdbc.password", "")).thenReturn("some-password");
        when(properties.size()).thenReturn(4);

        Set<String> profiles = new HashSet<String>();
        profiles.add("default");
        profiles.add("myprofile");
        when(loader.getProfiles(properties)).thenReturn(profiles);

        Optional<DbConfiguration> config = loader.getDbConfiguration("myprofile", properties);
        assertTrue(config.isPresent());
        assertFalse(Strings.isNullOrEmpty(config.get().getDriverClassName()));
        assertFalse(Strings.isNullOrEmpty(config.get().getUsername()));
        assertFalse(Strings.isNullOrEmpty(config.get().getUrl()));
        assertFalse(Strings.isNullOrEmpty(config.get().getPassword()));
    }

    @Test(expected = NameConfigNotFoundException.class)
    public void getDbConfiguration_InvalidProfileTest() {
        PropertyFileLoader loader = new PropertyFileLoader();
        Properties properties = mock(Properties.class);
        when(properties.getProperty("myprofile.jdbc.url")).thenReturn("jdbc:h2:file:~/test");
        when(properties.getProperty("myprofile.jdbc.username")).thenReturn("sa");
        when(properties.getProperty("myprofile.jdbc.driver")).thenReturn("org.h2.Driver");
        when(properties.getProperty("myprofile.jdbc.password", "")).thenReturn("some-password");
        when(properties.size()).thenReturn(4);

        loader.getDbConfiguration("some-invalid-profile", properties);
    }
}