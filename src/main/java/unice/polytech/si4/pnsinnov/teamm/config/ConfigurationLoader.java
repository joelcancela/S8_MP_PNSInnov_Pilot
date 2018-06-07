package unice.polytech.si4.pnsinnov.teamm.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Class ConfigurationLoader
 *
 * @author Joël CANCELA VAZ
 */
public class ConfigurationLoader {

	private static final Logger logger = LogManager.getLogger(ConfigurationLoader.class.getName());
	private static ConfigurationLoader instance = null;
	private String host;

	private ConfigurationLoader() {
	}

	public static ConfigurationLoader getInstance() {
		if (instance == null) {
			instance = new ConfigurationLoader();
			instance.initialize();
		}
		return instance;
	}

	private void initialize() {
		try {
			Properties prop = new Properties();
			prop.load(ConfigurationLoader.class.getResourceAsStream("/server.properties"));
			host = prop.getProperty("HOST_IP_PORT", "localhost:9000");

		} catch (IOException e) {
			logger.log(Level.INFO, "Cannot read mailer.properties file", e);
		}
	}

	public String getHost() {
		return host;
	}
}
