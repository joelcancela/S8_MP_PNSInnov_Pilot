package unice.polytech.si4.pnsinnov.teamm.drive;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Class Deleter
 *
 * @author Joël CANCELA VAZ
 * TODO: To edit when abstraction will be done
 */
@Path("deleteDrive")
public class FileDeleter {
	private static final Logger logger = LogManager.getLogger(FileDownloader.class);

	@FormParam("drive")
	String drive;

	@FormParam("fileid")
	String fileid;

	@POST
	public Response deleteFile(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		logger.log(Level.INFO, "DELETE REQUESTED : " + fileid + " - " + drive);

		if (fileid == null) {
			logger.log(Level.ERROR, "A file id must be provided");
			return Response.status(404).build();
		}

		if (drive.equals("gdrive")) {
			logger.log(Level.INFO, "DELETING FROM GDRIVE");
			GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

			try {
				GDrive.getGDrive().deleteFile(session, fileid);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		} else if (drive.equals("dropbox")) {
			logger.log(Level.INFO, "DELETING FROM DROPBOX");

			DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);

			try {
				session.getDropboxClient().files().deleteV2("id:" + fileid);
			} catch (DbxException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}

		return Response.ok().build();
	}
}
