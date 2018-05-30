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
@Path("downloadDrive")
public class Downloader {
	private static final Logger logger = LogManager.getLogger(Downloader.class);

	@QueryParam("filename") String filename;
	@QueryParam("filelink") String filelink;

	@GET
	public String get() {

		if (filename == null || filelink == null) {
			logger.log(Level.ERROR, "A filename and a filelink must be provided");
			return "Error";
		}
		try {
			Login.googleDrive.downloadFile(false, filename, filelink);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename + " Downloaded from " + filelink;
	}
}
