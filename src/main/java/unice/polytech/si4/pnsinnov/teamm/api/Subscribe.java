package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Nassim B on 6/8/18.
 */
@Path("subscribe")
public class Subscribe {
	private static final Logger logger = LogManager.getLogger(Subscribe.class);
	@POST
	public Response getHomepage(@Context HttpServletRequest request, @Context HttpServletResponse response, @FormParam("username") String username, @FormParam("password") String password) {
		logger.log(Level.INFO, "Subscribe page : " + username + " - " + password);
		if (Login.registerUser(username, password)) {
			HttpSession session = request.getSession();
			session.setAttribute("user.logged", Login.getUserID(username));
			try {
				response.sendRedirect(request.getContextPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return Response.ok(new Viewable("/index", null)).build();
		}
		return null;
	}
}
