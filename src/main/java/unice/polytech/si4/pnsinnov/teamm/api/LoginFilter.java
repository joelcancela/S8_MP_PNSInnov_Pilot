package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveOAuth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by Nassim B on 6/6/18.
 */
@Provider
public class LoginFilter implements ContainerRequestFilter {
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN).build();

	private static final Logger logger = LogManager.getLogger(LoginFilter.class.getName());

	@Context
	private ResourceInfo resinfo;

	@Context
	HttpServletRequest webRequest;

	@Override
	public void filter(ContainerRequestContext requestContext)
	{
		logger.log(Level.INFO, "REQUEST INTERCEPTED : " + requestContext.toString());
		logger.log(Level.INFO, "FROM : " + resinfo.getResourceClass());
		logger.log(Level.INFO, "Session : " + webRequest.getSession().getId());
		logger.log(Level.INFO, "Connected : " + webRequest.getSession().getAttribute("user.logged"));

		if (webRequest.getSession().getAttribute("user.logged") == null && (resinfo.getResourceClass() != Login.class && resinfo.getResourceClass() != GDriveOAuth.class)) {
			requestContext.abortWith(ACCESS_FORBIDDEN);
		}
	}
}