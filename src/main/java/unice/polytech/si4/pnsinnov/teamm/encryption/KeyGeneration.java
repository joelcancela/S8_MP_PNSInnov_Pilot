package unice.polytech.si4.pnsinnov.teamm.encryption;

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

	private final String CIPHER_ALGO = "AES";

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getString() {
		Optional<SecretKey> key = generateKey();
		if (key.isPresent()) {
			return Base64.getEncoder().encodeToString(key.get().getEncoded());
		}
		return "Impossible to retrieve a key";
	}

	public Optional<SecretKey> generateKey() {
		Optional<SecretKey> key = Optional.empty();
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER_ALGO);
			key = Optional.of(keyGenerator.generateKey());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return key;
	}
}
