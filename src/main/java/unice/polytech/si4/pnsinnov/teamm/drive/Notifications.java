package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;


/**
 * Created by Nassim B on 5/23/18.
 */
@Path("notifications")
public class Notifications {
	private static final Logger logger = LogManager.getLogger(Notifications.class);

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public void postHandle(@Context HttpHeaders headers) {
		MultivaluedMap<String, String> rh = headers.getRequestHeaders();
		String userID = rh.getFirst("x-goog-channel-id");
		String headersChange = rh.getFirst("x-goog-resource-state");
		if(userID.equals("userID") && headersChange.equals("change")){//FIXME: multiples sessions voir GDrive:75
			logger.log(Level.INFO, "Changes received for "+userID);
			try {
				Login.googleDrive.getChanges();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
