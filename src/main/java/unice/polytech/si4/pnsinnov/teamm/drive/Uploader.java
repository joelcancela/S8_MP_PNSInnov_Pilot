package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.encryption.FileEncryption;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * Created by Nassim B on 5/30/18.
 */
@Path("uploadDrive")
public class Uploader {

	private static final Logger logger = LogManager.getLogger(Uploader.class);

	@GET
	public String get(@QueryParam("userId") String userID) {
		String fileurl = FileEncryption.class.getResource("/tocrypt.txt").getFile();
		File file = new File(fileurl);
		StringBuilder stringBuilder = new StringBuilder();

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		try {
			logger.log(Level.INFO, "Try to upload file : " + file);
			((GDriveSession)Login.storageSessions.get(userID)).getDrive().uploadFile(false, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
