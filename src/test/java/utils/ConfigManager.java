package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Logger log = LoggerFactory.getLogger(ConfigManager.class);
    private static final Properties props = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(is);
            log.info("Configuration loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        // Allow system property overrides (useful for CI)
        String sysProp = System.getProperty(key);
        return sysProp != null ? sysProp : props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key, "false"));
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String baseUrl()           { return get("base.url"); }
    public String browser()           { return get("browser", "chromium"); }
    public boolean headless()         { return getBoolean("headless"); }
    public int slowMotion()           { return getInt("slow.motion.ms", 0); }
    public int defaultTimeout()       { return getInt("default.timeout", 30000); }
    public int navigationTimeout()    { return getInt("navigation.timeout", 30000); }
    public boolean screenshotOnFail() { return getBoolean("screenshot.on.failure"); }
    public String screenshotDir()     { return get("screenshot.dir", "target/screenshots"); }
    public boolean recordVideo()      { return getBoolean("record.video"); }
    public String videoDir()          { return get("video.dir", "target/videos"); }
}
