package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Nassim B on 6/4/18.
 */
@Path("drive-list")
public class ListingPage {
	private static final Logger logger = LogManager.getLogger(ListingPage.class.getName());

	@GET
	public Response getPage(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.log(Level.INFO, "session is : " + session);
			//request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
			//request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			map.put("ownFile", GDrive.getGDrive().classifyFiles(session));
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}

		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
	}
}
