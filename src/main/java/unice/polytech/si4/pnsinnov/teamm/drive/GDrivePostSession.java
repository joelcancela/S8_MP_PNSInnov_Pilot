package unice.polytech.si4.pnsinnov.teamm.drive;

import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Class GDrivePostSession
 *
 * @author Joël CANCELA VAZ
 */
@Path("GDrive")
public class GDrivePostSession {
	@GET
	public void instantiateGDrive(@Context HttpServletRequest request,
	                              @Context HttpServletResponse response) throws ServletException, IOException {

		GDrive googleDrive = Login.googleDrive;
		googleDrive.initialize();
		googleDrive.subscribeToChanges();

		request.setAttribute("list", googleDrive.getFilesList());
		request.getRequestDispatcher("/PrivateMemo/gdrive-list.jsp").forward(request, response);
	}
}

