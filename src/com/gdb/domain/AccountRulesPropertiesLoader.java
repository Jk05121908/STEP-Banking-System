package com.gdb.domain;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AccountRulesPropertiesLoader {

    private final Properties properties = new Properties();

    public AccountRulesPropertiesLoader(String filePath) {
        try (InputStream input = Files.newInputStream(Paths.get(filePath))) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to load rules from " + filePath, e);
        }
    }

    public AccountRulesPropertiesLoader(InputStream input) {
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to load properties", e);
        }
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}