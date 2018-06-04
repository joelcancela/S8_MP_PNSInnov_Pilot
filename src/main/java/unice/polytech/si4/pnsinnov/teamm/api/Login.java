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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
	public static List<User> users = new ArrayList();
	public static HashMap<String, StorageSession> storageSessions = new HashMap();
	//FIXME:  Multiple sessions with UUID

	@GET
	public Response authorizeDrive(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response,
	                               @QueryParam("drive") String driveType)  {
		String userid = UUID.randomUUID().toString();
		if (driveType.equals("google")) {
			GDriveSession gDriveSession = new GDriveSession(userid);
			Login.storageSessions.put(userid, gDriveSession);
			return Response.seeOther(gDriveSession.getAuthRequest().toURI()).build();
		}
		return Response.status(200).build();
	}
}
