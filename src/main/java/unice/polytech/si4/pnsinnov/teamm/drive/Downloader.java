package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.io.IOException;


/**
 * Created by Nassim B on 5/30/18.
 */
@Path("downloadDrive")
public class Downloader {
	private static final Logger logger = LogManager.getLogger(Downloader.class);

	@GET
	public String get(@QueryParam("fileid") String fileid) {
		if (fileid == null) {
			logger.log(Level.ERROR, "A file id must be provided");
			return "Error";
		}
		try {
			Login.googleDrive.downloadFile(false, fileid, null); //TODO : Currently exportedMime is mocked in method, must be provided by gui
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "File Downloaded";
	}
}
