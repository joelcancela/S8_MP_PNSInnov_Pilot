package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.crypto.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
	public void retrieveAndDecipherFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
		if (session == null) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		} else {
			if (encryptedFileId == null || encryptedFileId.isEmpty()) {
				request.setAttribute("error", "A target file to decrypt must be provided");
				try {
					request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
					request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
				} catch (IOException | ServletException e) {
					logger.log(Level.ERROR, e.getMessage());
				}
			}
			String downloadedPath = null;
			try {
				downloadedPath = GDrive.getGDrive().downloadFile(session, false, encryptedFileId, null); //TODO : Currently exportedMime is mocked in method, must be provided by gui
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
				request.setAttribute("success", "the file " + inputFile.getName() + " has been decrypted and uploaded as : " + inputFile.getName() + "-decrypted");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
				logger.log(Level.ERROR, e.getMessage());
				request.setAttribute("error", "An error occured while decrypting the file " + inputFile.getName());
			}
			try {
				request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			} catch (IOException | ServletException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
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
