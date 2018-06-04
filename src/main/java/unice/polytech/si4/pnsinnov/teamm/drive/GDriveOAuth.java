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
import java.util.logging.Logger;

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
								  @QueryParam("userId") String userId,
	                              @QueryParam("code") String code) throws ServletException, IOException {
		GDriveSession gDriveSession = (GDriveSession) Login.storageSessions.get(userId);
		gDriveSession.setCredential(code);
		gDriveSession.initialize();
		gDriveSession.getDrive().subscribeToChanges(userId);

		request.setAttribute("ownFile", gDriveSession.getDrive().classifyFiles());
		request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
	}
}
