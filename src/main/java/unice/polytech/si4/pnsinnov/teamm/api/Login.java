package unice.polytech.si4.pnsinnov.teamm.api;

import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class Login
 *
 * @author Joël CANCELA VAZ
 */
@Path("login")
public class Login {

	private static final Logger logger = Logger.getLogger(Login.class.getName());
	public static String userid = UUID.randomUUID().toString();
	public static GDrive googleDrive;
	public static GDriveSession gDriveSession = new GDriveSession(userid);
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
				OwnFile files = googleDrive.classifyFiles();
				/*logger.log(Level.WARNING, "RELOADING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors.toList()));
				for (File f : files) {
					if (f.getWebContentLink() == null) continue;
					logger.log(Level.INFO, "Cutting content link : " + f.getWebContentLink());
					if (!f.getWebContentLink().isEmpty()) {
						f.setWebContentLink(f.getWebContentLink().substring(0, f.getWebContentLink().indexOf('&')));
					}
					logger.log(Level.INFO, "New content link : " + f.getWebContentLink());
				}*/

				request.setAttribute("ownFile", files);
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			}
		}
		return Response.status(200).build();
	}

	@POST
	public void authorizeLogIn(@FormParam("username") String username,
							   @FormParam("password") String password) {
		logger.log(Level.INFO, username);
		logger.log(Level.INFO, password);
	}


	public static boolean isAlreadyLoggedIn() {
		return gDriveSession.getCredential() != null;
	}
}
