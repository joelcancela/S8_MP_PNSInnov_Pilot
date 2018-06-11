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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static unice.polytech.si4.pnsinnov.teamm.encryption.KeyGeneration.CIPHER_ALGO;

/**
 * Class FileEncryption used to encrypt files with AES
 *
 * @author Nassim BOUNOUAS
 * @author Joël CANCELA VAZ
 */
@Path("fileencryption")
public class FileEncryption extends Encryption {
	private static final Logger logger = LogManager.getLogger(FileEncryption.class);

	@QueryParam("fileid")
	String fileid;

	@GET
	public Response retrieveAndCipherFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		Map<String, Object> map = new HashMap<>();

		if (fileid == null || fileid.isEmpty()) {
			map.put("error", "A file id must be provided");
		} else {
			File file = null;
			try {
				String downloadedPath = GDrive.getGDrive().downloadFile(session, fileid, null);
				//TODO : Currently exportedMime is mocked in method, must be provided by gui
				logger.log(Level.INFO, "File downloaded to " + downloadedPath);
				file = new File(downloadedPath);
				File outFile = encryptFile(file, KeyGeneration.getKey());

				GDrive.getGDrive().uploadFile(session, false, outFile);

				map.put("success", "the file " + file.getName() + " has crypted and uploaded as : " + getNewName(file.getName(), true));

			} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
				logger.log(Level.ERROR, e.getMessage());
				map.put("error", "An error occured while crypting the file " + file.getName());
			}
		}
		try {
			map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
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

			outFile = new File(getNewName(inputFile.getPath(), true));
			outputStream = new FileOutputStream(outFile);
			outputStream.write(outputBytes);
		}
		outputStream.close();
		return outFile;
	}

}
