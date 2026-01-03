package ru.grabber.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Config {
    public static final Logger log = Logger.getLogger(Config.class);
    private static final Properties properties = new Properties();

    public void load(String file) {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(file)) {
            properties.load(input);
        } catch (IOException io) {
            log.error("When load file : %s , file", io);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
