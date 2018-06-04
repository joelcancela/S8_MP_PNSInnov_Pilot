package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by Nassim B on 5/29/18.
 */
@Path("generateKey")
public class KeyGeneration {
	private static final Logger logger = LogManager.getLogger(KeyGeneration.class);
	private final static String CIPHER_ALGO = "AES";
	private static SecretKey key;

	@GET
	public void getEncodedString(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("userId") String userID) {
		request.setAttribute("key", Base64.getEncoder().encodeToString(getKey().getEncoded()));
		try {
			request.setAttribute("ownFile", ((GDriveSession)Login.storageSessions.get(userID)).getDrive().classifyFiles());
			request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}

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
