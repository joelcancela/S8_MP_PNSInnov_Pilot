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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static unice.polytech.si4.pnsinnov.teamm.encryption.KeyGeneration.CIPHER_ALGO;

/**
 * Class FileEncryption used to encrypt files with AES
 *
 * @author Nassim BOUNOUAS
 * @author Joël CANCELA VAZ
 */
@Path("fileencryption")
public class FileEncryption {
	private static final Logger logger = LogManager.getLogger(FileEncryption.class);

	@QueryParam("fileid")
	String fileid;

	@GET
	public void retrieveAndCipherFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		if (session == null) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		} else {
			if (fileid == null || fileid.isEmpty()) {
				request.setAttribute("error", "A file id must be provided");
				try {
					request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
					request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
				} catch (IOException | ServletException e) {
					logger.log(Level.ERROR, e.getMessage());
				}
			}
			File file = null;
			try {
				String downloadedPath = GDrive.getGDrive().downloadFile(session, false, fileid, null);
				//TODO : Currently exportedMime is mocked in method, must be provided by gui
				logger.log(Level.INFO, "File downloaded to " + downloadedPath);
				file = new File(downloadedPath);
				File outFile = encryptFile(file, KeyGeneration.getKey());

				GDrive.getGDrive().uploadFile(session, false, outFile);

				request.setAttribute("success", "the file " + file.getName() + " has crypted and uploaded as : " + file.getName() + "-crypted");

			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
				logger.log(Level.ERROR, e.getMessage());
				request.setAttribute("error", "An error occured while crypting the file " + file.getName());
			}

			try {
				request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			} catch (IOException | ServletException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
	}

	public File encryptFile(File inputFile, SecretKey key) throws IOException, BadPaddingException,
			IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
		cipher.init(Cipher.ENCRYPT_MODE, key);

		File outFile;
		FileOutputStream outputStream;
		try (FileInputStream inputStream = new FileInputStream(inputFile)) {
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			outFile = new File(inputFile.getPath() + "-crypted");
			outputStream = new FileOutputStream(outFile);
			outputStream.write(outputBytes);
		}
		outputStream.close();
		return outFile;
	}

}
