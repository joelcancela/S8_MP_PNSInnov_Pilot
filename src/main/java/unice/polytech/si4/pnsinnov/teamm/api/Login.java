package unice.polytech.si4.pnsinnov.teamm.api;

import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class Login
 *
 * @author Joël CANCELA VAZ
 */
@Path("login")
public class Login {

	private static final Logger logger = Logger.getLogger(Login.class.getName());
	public static GDrive googleDrive;
	public static GDriveSession gDriveSession = new GDriveSession("skynet-id-00");
	//FIXME:  Multiple sessions with UUID

	@GET
	public Response authorizeDrive(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response,
	                               @QueryParam("drive") String driveType) throws
			IOException, ServletException {
		if (driveType.equals("google")) {
			if (Login.gDriveSession.getCredential() == null) {
				try {
					googleDrive = new GDrive();
				} catch (IOException | GeneralSecurityException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
				return Response.seeOther(gDriveSession.getAuthRequest().toURI()).build();
			} else {
				request.setAttribute("list", googleDrive.getFilesList());
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			}
		}
		return Response.status(200).build();
	}


	public static boolean isAlreadyLoggedIn() {
		return gDriveSession.getCredential() != null;
	}
}
