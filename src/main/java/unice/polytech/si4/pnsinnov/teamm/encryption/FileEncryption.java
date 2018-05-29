package unice.polytech.si4.pnsinnov.teamm.encryption;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by Nassim B on 5/28/18.
 */
@Path("fileencryption")
public class FileEncryption {

	private final String CIPHER_ALGO = "AES";
	private static final String KEY_ALGORITHM = "AES";

	@QueryParam("key") String key;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		String fileurl = FileEncryption.class.getResource("/tocrypt.txt").getFile();
		File file = new File(fileurl);
		StringBuilder stringBuilder = new StringBuilder();

		if(key.isEmpty()) return "A key must be provided";

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("##### JPNE #####");
			System.out.println(fileurl);
		}


		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), KEY_ALGORITHM));
			return Base64.encodeBase64URLSafeString(cipher.doFinal(stringBuilder.toString().getBytes()));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

}
