package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Class Login
 *
 * @author Joël CANCELA VAZ
 * TODO: To edit when abstraction will be done
 */
@Path("login")
public class Login {

	private static final Logger logger = LogManager.getLogger(Login.class.getName());
	private static HashMap<String, GDriveSession> driveSessions = new HashMap<>();
	private static HashMap<String, DropboxSession> dropboxSessions = new HashMap<>();
	private static HashMap<String, String> usersPassword = new HashMap<>();
	private static HashMap<String, String> usersUuid = new HashMap<>();

	@GET
	public Response authorizeDrive(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response,
	                               @QueryParam("drive") String driveType) throws URISyntaxException {
		if (driveType.equals("google")) {
			GDrive gdrive = GDrive.getGDrive();
			HttpSession session = request.getSession();
			String userid = session.getAttribute("user.logged").toString();
			GDriveSession gDriveSession = new GDriveSession(userid, gdrive.getFlow());
			driveSessions.put(userid, gDriveSession);
			return Response.seeOther(gDriveSession.getAuthRequest().toURI()).build();
		} else if (driveType.equals("dropbox")) {
			DropboxDrive dropboxDrive = DropboxDrive.getDropboxDrive();
			HttpSession session = request.getSession();
			String userid = session.getAttribute("user.logged").toString();
			DropboxSession dropboxSession = dropboxDrive.createNewSession(userid);
			dropboxSessions.put(userid, dropboxSession);
			URI uri = new URI(dropboxSession.getAuthURL(request));

			return Response.seeOther(uri).build();
		}
		return Response.status(200).build();
	}

	@POST
	public void authorizeLogIn(@Context HttpServletRequest request,
	                           @Context HttpServletResponse response,
	                           @FormParam("username") String username,
	                           @FormParam("password") String password) {
		if (Login.usersPassword.containsKey(username) && Login.usersPassword.get(username).equals(password)) {
			HttpSession session = request.getSession();
			session.setAttribute("user.logged", usersUuid.get(username));
			try {
				response.sendRedirect(request.getContextPath());
			} catch (IOException e) {
				try {
					logger.log(Level.ERROR, "Error while redirecting to drive-list"); //TODO : Handle this case properly
					response.sendRedirect("connection-failed");
				} catch (IOException e1) {
					logger.log(Level.ERROR, e.getMessage());
				}
			}
		}
	}

	public static GDriveSession getDriveSessions(String userID) {
		if (driveSessions.containsKey(userID)) {
			return driveSessions.get(userID);
		}
		return null;
	}

	public static DropboxSession getDropboxSession(String userID) {
		if (dropboxSessions.containsKey(userID)) {
			return dropboxSessions.get(userID);
		}
		return null;
	}

	public static boolean registerUser(String username, String password) {
		if (Login.usersPassword.containsKey(username)) {
			return false;
		}
		Login.usersPassword.put(username, password);
		String userid = UUID.randomUUID().toString();
		Login.usersUuid.put(username, userid);
		return true;
	}

	public static String getUserID(String username) {
		if (usersUuid.containsKey(username)) {
			return usersUuid.get(username);
		}
		return null;
	}

	public static List<String> getAvailableUsers() {
		return new ArrayList<>(usersPassword.keySet());
	}

	public static String retrieverUserIDFromCookie(HttpServletRequest request) {
		Object attribute = request.getSession().getAttribute("user.logged");
		if (attribute != null) {
			return attribute.toString();
		}
		return null;
	}

	public static GDriveSession retrieveDriveSessionFromCookie(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user = session.getAttribute("user.logged").toString();//FIXME to change user.logged and dropbox
		logger.log(Level.INFO, "Retrieve drive gDriveSession for : ", user);
		GDriveSession gsession = Login.getDriveSessions(user);
		return gsession;
	}

	public static DropboxSession retrieveDropboxSessionFromCookie(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String user = session.getAttribute("user.logged").toString();
		logger.log(Level.INFO, "Retrive drive gDriveSession for :" + user);
		DropboxSession dropboxSession = Login.getDropboxSession(user);
		return dropboxSession;
	}
}
