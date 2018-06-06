package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;


/**
 * Created by Nassim B on 6/4/18.
 */
@Path("drive-list")
public class ListingPage {
	private static final Logger logger = LogManager.getLogger(ListingPage.class.getName());

	@GET
	public void getPage(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		if (session == null) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		} else {
			try {
				logger.log(Level.INFO, "session is : " + session);
				request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			} catch (ServletException | IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
	}
}
