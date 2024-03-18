package org.dreds20.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class Config {
    private final PropertiesConfiguration configuration;
    private Config() {
        Configurations configurations = new Configurations();
        try {
            configuration = configurations.properties(new File("config.properties"));
        } catch (ConfigurationException e) {
            throw new RuntimeException("Exception thrown while loading properties", e);
        }
    }

    private static class ConfigSingleton {
        private static final Config INSTANCE = new Config();
    }

    public static PropertiesConfiguration get() {
        return ConfigSingleton.INSTANCE.configuration;
    }
}
