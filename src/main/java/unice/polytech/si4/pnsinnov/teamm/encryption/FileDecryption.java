package unice.polytech.si4.pnsinnov.teamm.encryption;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
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
public class FileDecryption extends Encryption {
	private static final Logger logger = LogManager.getLogger(FileDecryption.class);
	@QueryParam("encryptedFileId")
	String encryptedFileId;

	@QueryParam("drive")
	String drive;

	@GET
	public Response retrieveAndDecipherFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		// TODO: 13/06/2018 DUPLICATION EVERYWHERE
		Map<String, Object> map = new HashMap<>();
		if (encryptedFileId == null || encryptedFileId.isEmpty()) {
			map.put("error", "A target file to decrypt must be provided");
		} else {
			if (drive.equals("gdrive")) {
				GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
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
					map.put("success", "the file " + inputFile.getName() + " has been decrypted and uploaded as : " + getNewName(inputFile.getPath(), false));
				} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
					logger.log(Level.ERROR, e.getMessage());
					map.put("error", "An error occured while decrypting the file " + inputFile.getName());
				}
				try {
					logger.log(Level.INFO, "session is : " + session);
					map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
					return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
				} catch (IOException e) {
					logger.log(Level.ERROR, e.getMessage());
				}
			} else if (drive.equals("dropbox")){
				DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
				File downloadedFile = null;
				try {
					downloadedFile = DropboxDrive.getDropboxDrive().downloadFile(session, encryptedFileId);
				} catch (DbxException | IOException e) {
					logger.log(Level.ERROR, e.getMessage());
				}
				logger.log(Level.INFO, "File downloaded to " + downloadedFile);
				try {
					java.nio.file.Path currentRelativePath = Paths.get("");
					java.nio.file.Path destination = Paths.get(currentRelativePath.toAbsolutePath().toString() + "/downloads");
					File outFile = decryptFile(downloadedFile, KeyGeneration.getKey(), destination);
					DropboxDrive.getDropboxDrive().uploadFile(session, outFile);
					map.put("success", "the file " + downloadedFile.getName() + " has been decrypted and uploaded as : " + getNewName(downloadedFile.getPath(), false));
				} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | DbxException | IOException e) {
					logger.log(Level.ERROR, e.getMessage());
					map.put("error", "An error occured while decrypting the file " + downloadedFile.getName());
				}
				logger.log(Level.INFO, "session is : " + session);
				map.put("fileRepresentation", DropboxDrive.getDropboxDrive().buildFileTree(session));
				return Response.ok(new Viewable("/dropbox-list.jsp", map)).build();
			}
		}
		return null; // TODO: 13/06/2018 change this
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
		logger.log(Level.INFO, "OUTPUT FILE : " + destination.toString() + "/" + getNewName(cryptedFile.getName(), false));
		File outFile = new File(destination.toString() + "/" + getNewName(cryptedFile.getName(), false));
		FileOutputStream outputStream = new FileOutputStream(outFile);
		outputStream.write(outputBytes);
		inputStream.close();
		outputStream.close();
		return outFile;
	}

}
