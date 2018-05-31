package unice.polytech.si4.pnsinnov.teamm.drive;

import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static unice.polytech.si4.pnsinnov.teamm.api.Login.googleDrive;

/**
 * Class GDriveOAuth
 *
 * @author Joël CANCELA VAZ
 */
@Path("GDriveOAuth")
public class GDriveOAuth {
	private static final Logger logger = Logger.getLogger(GDriveOAuth.class.getName());

	@GET
	public void receiveCodeGDrive(@Context HttpServletRequest request,
	                              @Context HttpServletResponse response,
	                              @QueryParam("code") String code) throws ServletException, IOException {
		GDriveSession gDriveSession = Login.gDriveSession;
		if (gDriveSession.credential == null) {
			try {
				gDriveSession.setCredential(code);
			} catch (IOException e) {
				logger.log(Level.INFO, e.getMessage());
			}
		}

		googleDrive.initialize();
		googleDrive.subscribeToChanges();

		//request.setAttribute("list", googleDrive.getFilesList());
		request.setAttribute("ownFile", googleDrive.classifyFiles());
		request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
	}
}
