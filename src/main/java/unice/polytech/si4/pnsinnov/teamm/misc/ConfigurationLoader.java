package unice.polytech.si4.pnsinnov.teamm.misc;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class x
 *
 * @author Joël CANCELA VAZ
 */
public class ConfigurationLoader {


	private static final Logger log = Logger.getLogger(Logger.class.getName());
	/** Constructeur privé */
	private ConfigurationLoader()
	{}

	/** Instance unique non préinitialisée */
	private static ConfigurationLoader INSTANCE = null;

	private static String host = "localhost";

	/** Point d'accès pour l'instance unique du singleton */
	public static ConfigurationLoader getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ConfigurationLoader();

		}
		return INSTANCE;
	}

	public void initialize() {
		try {
			Properties prop = new Properties();
			prop.load(ConfigurationLoader.class.getResourceAsStream("/server.properties"));
			host = prop.getProperty("HOST_IP_PORT", "localhost:9000");

		} catch (IOException e) {
			log.log(Level.INFO, "Cannot read mailer.properties file", e);
		}
	}

	public static String getHost() {
		return host;
	}
}
