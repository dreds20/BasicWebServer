package org.dreds20.config;

import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.Locale;

public class Config {
    private final PropertiesConfiguration configuration;
    private final EnvironmentConfiguration environmentConfiguration;

    private Config() {
        Configurations configurations = new Configurations();
        try {
            configuration = configurations.properties(new File("config.properties"));
            environmentConfiguration= new EnvironmentConfiguration();
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

    private static String parseToEnvVar(String key) {
        return key.replace(".", "_").toUpperCase(Locale.ENGLISH);
    }

    public static String getString(String key) {
        String envVar = parseToEnvVar(key);
        Config instance = ConfigSingleton.INSTANCE;
        if (instance.environmentConfiguration.containsKey(envVar)) {
            return instance.environmentConfiguration.getString(envVar);
        }
        return instance.configuration.getString(key);
    }
}
