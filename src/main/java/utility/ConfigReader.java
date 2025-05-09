package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read configuration properties
 */

public class ConfigReader {

	public static Properties properties = new Properties();
	private static final Logger logger = LogManager.getLogger(ConfigReader.class);
	private static final String CONFIG_PATH = "\\src\\main\\resource\\config.properties";
//	private static final String Path = "C:\\Users\\Dinesh\\eclipse-workspace\\RahulShety\\src\\main\\resource\\config.properties ";
	private static boolean isInitialized = false;

	private ConfigReader() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Initializes the properties object
	 */
	public static void initialize() {
		if (!isInitialized) {
			try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+CONFIG_PATH)) {
				properties.load(fis);
				logger.info("Configuration loaded successfully from: {}", CONFIG_PATH);
				isInitialized = true;
			} catch (IOException e) {
				logger.error("Failed to load configuration: {}", e.getMessage());
				throw new RuntimeException("Failed to load configuration file: " + CONFIG_PATH, e);
			}
		}
	}

	/**
	 * Gets property value by key
	 * 
	 * @param key Property key
	 * @return Property value
	 */
	public static String getProperty(String key) {
		if (!isInitialized) {
			initialize();
		}

		String value = properties.getProperty(key);
		if (value == null) {
			logger.warn("Property not found: {}", key);
		}
		return value;
	}

	/**
	 * Gets property value by key with default value
	 * 
	 * @param key          Property key
	 * @param defaultValue Default value if property not found
	 * @return Property value or default value
	 */
	public static String getProperty(String key, String defaultValue) {
		if (!isInitialized) {
			initialize();
		}

		return properties.getProperty(key, defaultValue);
	}

	/**
	 * Sets a property value
	 * 
	 * @param key   Property key
	 * @param value Property value
	 */
	public static void setProperty(String key, String value) {
		if (!isInitialized) {
			initialize();
		}

		properties.setProperty(key, value);
		logger.info("Property set: {} = {}", key, value);
	}
}
