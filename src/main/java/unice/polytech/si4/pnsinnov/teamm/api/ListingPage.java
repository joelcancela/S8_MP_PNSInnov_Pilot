package unice.polytech.si4.pnsinnov.teamm.api;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nassim B on 6/4/18.
 */
@Path("drive-list")
public class ListingPage {
	private static final Logger logger = Logger.getLogger(ListingPage.class.getName());
	@GET
	public void getPage(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		Cookie cookies[] = request.getCookies();
		for (Cookie c : cookies) {
			logger.log(Level.INFO, "FOUND COOKIE : " + c.getName() + " Valued : " + c.getValue());
		}
		//request.setAttribute("posts", "UN MOT");
		try {
			request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Imposssible to dispatch gdrive-list.jsp");
		}
	}
}
