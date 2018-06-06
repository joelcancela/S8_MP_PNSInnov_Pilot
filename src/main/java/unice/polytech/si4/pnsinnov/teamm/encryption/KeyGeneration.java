package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Class KeyGeneration used to generate keys with AES 128
 *
 * @author Nassim BOUNOUAS
 * @author Joël CANCELA VAZ
 */
@Path("generateKey")
public class KeyGeneration {
	private static final Logger logger = LogManager.getLogger(KeyGeneration.class);
	static final String CIPHER_ALGO = "AES";
	private static SecretKey key;

	@GET
	public Response getEncodedString(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Map<String, Object> map = new HashMap();
		map.put("key", Base64.getEncoder().encodeToString(getKey().getEncoded()));

		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build(); // TODO : Create a dedicated page to key retrieving
	}

	static SecretKey getKey() {
		if (key == null) {
			KeyGenerator keyGenerator = null;
			try {
				keyGenerator = KeyGenerator.getInstance(CIPHER_ALGO);
			} catch (NoSuchAlgorithmException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
			key = keyGenerator.generateKey();//FIXME: This could break?
		}
		return key;
	}
}
