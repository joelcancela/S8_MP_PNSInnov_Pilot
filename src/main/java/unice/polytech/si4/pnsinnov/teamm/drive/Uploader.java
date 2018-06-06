package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.encryption.FileEncryption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
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
	public String get(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		if (session == null) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
			return "";
		} else {
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
				GDrive.getGDrive().uploadFile(session, false, file);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}

			return stringBuilder.toString();
		}
	}
}
