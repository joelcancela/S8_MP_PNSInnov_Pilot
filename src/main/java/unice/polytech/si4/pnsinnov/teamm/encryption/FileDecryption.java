package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Nassim B on 5/28/18.
 */
@Path("filedecryption")
public class FileDecryption {
	private static final Logger logger = LogManager.getLogger(FileDecryption.class);
	private final String CIPHER_ALGO = "AES";
	private static final String KEY_ALGORITHM = "AES";

	@QueryParam("key") String key;
	@QueryParam("encrypted") String encrypted;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {

		if(key.isEmpty()) return "A key must be provided";

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM));
			return new String (cipher.doFinal(Base64.decodeBase64(encrypted)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			logger.log(Level.ERROR, e.getMessage());
		}

		return "Encrypted";
	}

}
