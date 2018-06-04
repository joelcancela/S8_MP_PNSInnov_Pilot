package unice.polytech.si4.pnsinnov.teamm.api;

import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
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
	private static HashMap<String, GDriveSession> driveSessions = new HashMap();

	@GET
	public Response authorizeDrive(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response,
	                               @QueryParam("drive") String driveType) {
		if (driveType.equals("google")) {
			GDrive gdrive = GDrive.getGDrive();
			String userid = UUID.randomUUID().toString();
			GDriveSession gDriveSession = new GDriveSession(userid, gdrive.getFlow());
			driveSessions.put(userid, gDriveSession);
			response.addCookie(new Cookie("userID", userid));
			return Response.seeOther(gDriveSession.getAuthRequest().toURI()).build();
			/*
			 else {
				OwnFile files = googleDrive.classifyFiles();

				request.setAttribute("ownFile", files);
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			}*/
		}
		return Response.status(200).build();
	}

	public static GDriveSession getDriveSessions(String userID) {
		if (driveSessions.containsKey(userID)) {
			return driveSessions.get(userID);
		} else {
			String uiknown = "";
			for (String s : driveSessions.keySet()) {
				uiknown +=  "||" + s + "||";
			}
			throw new RuntimeException("User ID " + userID + " not found, hashmap size : " + driveSessions.size()  + " containing : " + uiknown);
		}
	}
}
