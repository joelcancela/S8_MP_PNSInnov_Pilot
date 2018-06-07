package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Class Deleter
 *
 * @author Joël CANCELA VAZ
 */
@Path("deleteDrive")
public class Deleter {
	private static final Logger logger = LogManager.getLogger(Downloader.class);

	@POST
	public Response deleteFile(@Context HttpServletRequest request, @Context HttpServletResponse response,
	                           @FormParam("fileid") String fileid) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		if (fileid == null) {
			logger.log(Level.ERROR, "A file id must be provided");
			return Response.status(404).build();
		}
		try {
			GDrive.getGDrive().deleteFile(session, fileid);
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return Response.ok().build();
	}
}
