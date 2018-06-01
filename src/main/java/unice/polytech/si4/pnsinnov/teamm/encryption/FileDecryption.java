package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static unice.polytech.si4.pnsinnov.teamm.api.Login.googleDrive;

/**
 * Created by Nassim B on 5/28/18.
 */
@Path("filedecryption")
public class FileDecryption {
	private static final Logger logger = LogManager.getLogger(FileDecryption.class);
	private final String CIPHER_ALGO = "AES";
	private static final String KEY_ALGORITHM = "AES";

	@QueryParam("encrypted") String encrypted;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	public void getIt(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		if(encrypted.isEmpty() || encrypted == null) {
			request.setAttribute("error", "A target file to decrypt must be provided");
			try {
				request.setAttribute("ownFile", googleDrive.classifyFiles());
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			} catch (IOException | ServletException e) {
				e.printStackTrace();
			}
		}

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.DECRYPT_MODE, KeyGeneration.getKey());

			java.nio.file.Path currentRelativePath = Paths.get("");
			java.nio.file.Path destination = Paths.get(currentRelativePath.toAbsolutePath().toString() + "/downloads");

			File inputFile =new File(destination + "/" + encrypted);

			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			logger.log(Level.INFO, "OUTPUT FILE : " + destination.toString() + "/" + inputFile.getName() + "-decrypted");
			File outFile = new File(destination.toString() + "/" + inputFile.getName() + "-decrypted");
			FileOutputStream outputStream = new FileOutputStream(outFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

			Login.googleDrive.uploadFile(false, outFile);


			request.setAttribute("success", "the file " + inputFile.getName() + " has been decrypted and uploaded as : " + inputFile.getName() + "-decrypted");
			//return new String (cipher.doFinal(Base64.decodeBase64(encrypted)));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException |IOException e) {
			logger.log(Level.ERROR, e.getMessage());
			request.setAttribute("error", "An error occured while decrypting the file " + encrypted);
		}

		try {
			request.setAttribute("ownFile", googleDrive.classifyFiles());
			request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}

}
