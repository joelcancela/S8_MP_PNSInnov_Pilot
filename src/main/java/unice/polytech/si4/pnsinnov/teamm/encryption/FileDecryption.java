package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.crypto.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class FileDecryption used to decrypt files with AES
 *
 * @author Nassim BOUNOUAS
 * @author Joël CANCELA VAZ
 */
@Path("filedecryption")
public class FileDecryption {
	private static final Logger logger = LogManager.getLogger(FileDecryption.class);
	@QueryParam("encryptedFileId")
	String encryptedFileId;

	@GET
	public Response retrieveAndDecipherFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
		Map<String, Object> map = new HashMap<>();

		if (encryptedFileId == null || encryptedFileId.isEmpty()) {
			map.put("error", "A target file to decrypt must be provided");
		} else {
			String downloadedPath = null;
			try {
				downloadedPath = GDrive.getGDrive().downloadFile(session, encryptedFileId, null); //TODO : Currently exportedMime is mocked in method, must be provided by gui
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
			logger.log(Level.INFO, "File downloaded to " + downloadedPath);
			File inputFile = new File(downloadedPath);
			try {
				java.nio.file.Path currentRelativePath = Paths.get("");
				java.nio.file.Path destination = Paths.get(currentRelativePath.toAbsolutePath().toString() + "/downloads");
				File outFile = decryptFile(inputFile, KeyGeneration.getKey(), destination);
				GDrive.getGDrive().uploadFile(session, false, outFile);
				map.put("success", "the file " + inputFile.getName() + " has been decrypted and uploaded as : " + inputFile.getName() + "-decrypted");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
				logger.log(Level.ERROR, e.getMessage());
				map.put("error", "An error occured while decrypting the file " + inputFile.getName());
			}


			try {
				logger.log(Level.INFO, "session is : " + session);
				map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
	}

	public File decryptFile(File cryptedFile, SecretKey key, java.nio.file.Path destination) throws
			NoSuchPaddingException,
			NoSuchAlgorithmException,
			InvalidKeyException, IOException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(KeyGeneration.CIPHER_ALGO);
		cipher.init(Cipher.DECRYPT_MODE, key);
		FileInputStream inputStream = new FileInputStream(cryptedFile);
		byte[] inputBytes = new byte[(int) cryptedFile.length()];
		inputStream.read(inputBytes);
		byte[] outputBytes = cipher.doFinal(inputBytes);
		logger.log(Level.INFO, "OUTPUT FILE : " + destination.toString() + "/" + cryptedFile.getName() + "-decrypted");
		File outFile = new File(destination.toString() + "/" + cryptedFile.getName() + "-decrypted");
		FileOutputStream outputStream = new FileOutputStream(outFile);
		outputStream.write(outputBytes);
		inputStream.close();
		outputStream.close();
		return outFile;
	}

}
