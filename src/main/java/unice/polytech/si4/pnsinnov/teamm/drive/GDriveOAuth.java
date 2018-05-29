package unice.polytech.si4.pnsinnov.teamm.drive;

import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class GDriveOAuth
 *
 * @author Joël CANCELA VAZ
 */
@Path("GDriveOAuth")
public class GDriveOAuth {
	private static final Logger logger = Logger.getLogger(GDriveOAuth.class.getName());

	@GET
	public Response receiveCodeGDrive(@QueryParam("code") String code) {
		GDriveSession gDriveSession = Login.gDriveSession;
		if (gDriveSession.credential == null) {
			try {
				gDriveSession.setCredential(code);
			} catch (IOException e) {
				logger.log(Level.INFO, e.getMessage());
			}
		}
		return Response.status(200).build();
	}
}
