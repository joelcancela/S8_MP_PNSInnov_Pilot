package unice.polytech.si4.pnsinnov.teamm.api;

import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
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
	private GDrive googleDrive;

	@GET
	public void instantiateGDrive(@Context HttpServletRequest request,
	                              @Context HttpServletResponse response,
	                              @QueryParam("drive") String driveType) throws
			IOException, ServletException {//FIXME: UGLY TEMP
		// FIX for multiples
		// drives
		// types
		if (driveType.equals("google")) {
			googleDrive = new GDrive();
			googleDrive.initialize();
			googleDrive.subscribeToChanges();
			try {
				logger.log(Level.INFO, googleDrive.getFilesList().toString());
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		request.setAttribute("list", googleDrive.getFilesList());
		request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
	}

}
