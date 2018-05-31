package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

/**
 * Created by Nassim B on 5/29/18.
 */
@Path("generateKey")
public class KeyGeneration {
	private static final Logger logger = LogManager.getLogger(KeyGeneration.class);
	private final static String CIPHER_ALGO = "AES";
	private static SecretKey key;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getEncodedString() {
		return Base64.getEncoder().encodeToString(getKey().getEncoded());
	}

	public static SecretKey getKey() {
		if (key == null) {
			KeyGenerator keyGenerator = null;
			try {
				keyGenerator = KeyGenerator.getInstance(CIPHER_ALGO);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			key = keyGenerator.generateKey();
		}
		return key;
	}
}
