package unice.polytech.si4.pnsinnov.teamm.api;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by Nassim B on 6/5/18.
 */
@Path("logout")
public class Logout {

	@GET
	public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Cookie logoutCookie = new Cookie("userID", "");
		logoutCookie.setMaxAge(0);
		response.addCookie(logoutCookie);
		try {
			response.sendRedirect("/PrivateMemo");
		} catch (IOException e) {
			throw new RuntimeException("Error while redirecting to home"); // TODO : Handle this case properly
		}
	}
}
