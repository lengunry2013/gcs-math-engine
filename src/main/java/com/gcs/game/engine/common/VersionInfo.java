package com.gcs.game.engine.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

@Slf4j
@Data
public class VersionInfo {
    private String version;
    private String versionDate;

    private static HashMap<String, VersionInfo> instances = new HashMap<>();

    public VersionInfo(String fileName) {
        final Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(fileName));
            version = properties.getProperty("version");
            versionDate = properties.getProperty("versionDate");
            StringBuilder strB = new StringBuilder();
            strB.append("MathEngine-Version: ").append(version).append(",VersionDate: ").append(versionDate);
            log.debug(strB.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
