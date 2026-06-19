package com.qa.dummyjson.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";

    // Static block loads the file automatically once during runtime execution
    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL ERROR: Could not load config.properties from path: " + CONFIG_FILE_PATH, e);
        }
    }

    /**
     * Fetches the value associated with the configuration key.
     * @param key The property key string
     * @return The configuration value string
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("CONFIG ERROR: Key '" + key + "' was not found inside config.properties file!");
        }
        return value.trim();
    }
}