package com.gcs.game.utils;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
@Slf4j
public class SimulationConfReader {

    public static final String KEY_CLASS_PATH_MODEL_FACTORY = "classpath.GameModelFactory";

    private static final String FILE_NAME = "input/simulation.conf";

    private static Properties properties = null;

    public static String getStringValue(String key) {
        InputStreamReader reader = null;
        try {
            if (SimulationConfReader.properties == null) {
                File file = new File(FILE_NAME);
                if (file.exists()) {
                    reader = new InputStreamReader(new FileInputStream(FILE_NAME), "utf-8");
                    SimulationConfReader.properties = new Properties();
                    SimulationConfReader.properties.load(reader);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        if (SimulationConfReader.properties != null) {
            return (String) SimulationConfReader.properties.get(key);
        }
        return null;
    }

}
