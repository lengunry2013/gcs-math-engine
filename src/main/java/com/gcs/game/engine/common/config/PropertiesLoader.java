package com.gcs.game.engine.common.config;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {
    private static final Properties PROPERTIES = new Properties();

    public PropertiesLoader(String propertiesFileName) {
        try {
            String rootPath = System.getProperty("user.dir").replace("\\", "/");
            StringBuilder fileName = new StringBuilder();
            fileName.append(rootPath).append(File.separator).append(propertiesFileName);
            File newFile = new File(fileName.toString());
            if (newFile.exists()) {
                @Cleanup
                InputStream propertiesFile = new FileInputStream(newFile);
                PROPERTIES.load(propertiesFile);
            } else {
                PROPERTIES.load(this.getClass().getClassLoader().getResourceAsStream(propertiesFileName));
            }
        } catch (IOException e) {
            log.error("Cannot load properties file: {}", propertiesFileName, e);
        }
    }

    public PropertiesLoader(String propertiesFileName, boolean strict) throws IOException {
        File newFile = new File(propertiesFileName);

        try (InputStream propertiesFile = new FileInputStream(newFile)) {
            PROPERTIES.load(propertiesFile);
        } catch (IOException e) {
            log.error("Cannot load properties file: {}", propertiesFileName, e);
            if (strict) throw e;
        }
    }

    public String getProperty(String key) {
        String property = PROPERTIES.getProperty(key);

        if (property == null || property.trim().length() == 0) {
            property = null;
        }

        return property;
    }

    public boolean saveProperty(String propertiesName, Map<String, String> properties) {
        Properties tempProperties = new Properties();
        try {
            for (String key : properties.keySet()) {
                tempProperties.put(key, properties.get(key));
            }
        } catch (Exception e) {
            return false;
        }
        return saveProperty(propertiesName, tempProperties);
    }

    public boolean saveProperty(String propertiesName, Properties properties) {
        File propertiesFile = new File(propertiesName);
        boolean result = true;
        try (OutputStream output = new FileOutputStream(propertiesFile, false)) {
            if (!propertiesFile.exists() && !propertiesFile.createNewFile()) {
                return false;
            }
            properties.store(output, null);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
