package unice.polytech.si4.pnsinnov.teamm.drive.gdrive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;


/**
 * Created by Nassim B on 5/23/18.
 */
@Path("GDriveChanges")
public class GDriveChanges {
	private static final Logger logger = LogManager.getLogger(GDriveChanges.class);

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public void postHandle(@Context HttpHeaders headers, @QueryParam("userID") String userId) {
		MultivaluedMap<String, String> rh = headers.getRequestHeaders();
		String userID = rh.getFirst("x-goog-channel-id");
		String headersChange = rh.getFirst("x-goog-resource-state");
		//FIXME : Multiple RuleSet
		if(Login.getDriveSessions(userID) != null){
			logger.log(Level.INFO, "Changes received for ["+userID+"] headerChange:"+headersChange);
			try {
				GDrive.getGDrive().getChanges(Login.getDriveSessions(userID));
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
	}
}
