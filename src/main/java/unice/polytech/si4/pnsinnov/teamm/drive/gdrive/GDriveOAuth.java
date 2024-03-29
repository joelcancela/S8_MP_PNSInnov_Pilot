package unice.polytech.si4.pnsinnov.teamm.drive.gdrive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.security.GeneralSecurityException;



/**
 * Class GDriveOAuth used as Callback for google authentication Service
 *
 * @author Joël CANCELA VAZ
 * @author Nassim BOUNOUAS
 */
@Path("GDriveOAuth")
public class GDriveOAuth {
	private static final Logger logger = LogManager.getLogger(GDriveOAuth.class.getName());

	@GET
	public void receiveCodeGDrive(@Context HttpServletRequest request,
	                              @Context HttpServletResponse response,
	                              @QueryParam("code") String code) throws IOException {
		String userId = request.getSession().getAttribute("user.logged").toString();


		if (userId == null) {
			throw new RuntimeException("User ID can't be retrieved from cookies");
		}

		GDriveSession gDriveSession = Login.getDriveSessions(userId);
		try {
			gDriveSession.setCredential(code);
		} catch (GeneralSecurityException e) {
			logger.log(Level.ERROR, "Error occurred while set google gDriveSession credentials");
		}

		GDrive.getGDrive().subscribeToChanges(gDriveSession);

		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.sendRedirect("drive-list?drive=gdrive");
	}
}
