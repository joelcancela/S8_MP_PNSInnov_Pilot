package unice.polytech.si4.pnsinnov.teamm.api;

import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
	//FIXME: LATER Multiple session
	public static GDrive googleDrive;
	public static GDriveSession gDriveSession = new GDriveSession();

	@GET
	public Response authorizeGDrive(@QueryParam("drive") String driveType) throws
			IOException, ServletException {//FIXME: UGLY TEMP
		// FIX for multiples
		// drives
		// types
		if (driveType.equals("google")) {
			try {
				googleDrive = new GDrive();
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			return Response.seeOther(gDriveSession.getAuthRequest().toURI()).build();
		}
		return Response.status(200).build();
	}

}
