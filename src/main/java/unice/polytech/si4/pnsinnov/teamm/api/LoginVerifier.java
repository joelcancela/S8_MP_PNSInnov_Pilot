package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxOAuth;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveOAuth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Nassim B on 6/6/18.
 * TODO: To edit when abstraction will be done
 */
@Provider
public class LoginVerifier implements ContainerRequestFilter {
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

	private static final Logger logger = LogManager.getLogger(LoginVerifier.class.getName());

	@Context
	private ResourceInfo resinfo;

	@Context
	HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) {
		HttpSession session = webRequest.getSession();
		Object loggedAttribute = session.getAttribute("user.logged");
		String loggedName = (loggedAttribute == null) ? "" : loggedAttribute.toString();
		logger.log(Level.INFO, "REQUEST INTERCEPTED : " + requestContext.toString());
		logger.log(Level.INFO, "FROM : " + resinfo.getResourceClass());
		logger.log(Level.INFO, "Parameter : " + webRequest.getParameter("drive"));
		logger.log(Level.INFO, "Session : " + (session == null));
		logger.log(Level.INFO, "Connected : " + loggedAttribute == null);
		logger.log(Level.INFO, "logged attribute : " + loggedName);
		logger.log(Level.INFO, "Found : " + (Login.getDriveSessions(loggedName) == null));
		List<Class> allowedClass = Arrays.asList(DropboxOAuth.class, GDriveOAuth.class, Login.class, Logout.class,
				Subscribe.class, DriveLister.class);
		boolean notInLoginPage = allowedClass.stream().allMatch(e -> e != resinfo.getResourceClass());
		boolean gdriveConnected = Login.getDriveSessions(loggedName) != null;
		logger.log(Level.INFO, "Gdrive Connected : " + gdriveConnected);
		boolean dropboxConnected = Login.getDropboxSession(loggedName) != null;
		logger.log(Level.INFO, "Dropbox Connected : " + dropboxConnected);
		boolean userNotExist = (!gdriveConnected && !dropboxConnected);

		logger.log(Level.INFO, "Redirecting to 403 : " + (session == null || ((loggedAttribute == null) && notInLoginPage) || (userNotExist && notInLoginPage)));
		if (session == null || ((loggedAttribute == null) && notInLoginPage) || (session == null || ((loggedAttribute == null) && notInLoginPage) || (userNotExist && notInLoginPage))) {
			requestContext.abortWith(ACCESS_FORBIDDEN);
		}

		// If request on listing with the drive as parameter
		if (resinfo.getResourceClass() == DriveLister.class && webRequest.getParameter("drive") != null) {
			logger.log(Level.INFO, "CONNECTION DRIVELISTER CHECKING ");
			logger.log(Level.INFO, "Parameter : " + webRequest.getParameter("drive"));
			logger.log(Level.INFO, "Gdrive not Connected : " + !gdriveConnected);
			logger.log(Level.INFO, "In gdrive : " + (webRequest.getParameter("drive").equals("gdrive")));
			logger.log(Level.INFO, "Dropbox not Connected : " + !dropboxConnected);
			logger.log(Level.INFO, "In dropbox : " + (webRequest.getParameter("drive").equals("dropbox")));

			if (webRequest.getParameter("drive").equals("gdrive") && !gdriveConnected) {
				logger.log(Level.INFO, "CONNECTION DRIVELISTER CHECKING - DRIVE CASE");
				requestContext.abortWith(ACCESS_FORBIDDEN);
			}
			if (webRequest.getParameter("drive").equals("dropbox") && !dropboxConnected) {
				logger.log(Level.INFO, "CONNECTION DRIVELISTER CHECKING - DRIVE ");
				requestContext.abortWith(ACCESS_FORBIDDEN);
			}
		}
	}
}