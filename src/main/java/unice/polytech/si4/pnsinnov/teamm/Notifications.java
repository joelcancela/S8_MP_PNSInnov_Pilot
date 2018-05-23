package unice.polytech.si4.pnsinnov.teamm;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import java.util.stream.Collectors;


/**
 * Created by Nassim B on 5/23/18.
 */
@Path("notifications")
public class Notifications {
	private static final Logger logger = LogManager.getLogger(Notifications.class);

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Got it!";
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String postHandle(@Context HttpHeaders headers) {
		MultivaluedMap<String, String> rh = headers.getRequestHeaders();
		String str = rh.entrySet()
				.stream()
				.map(e -> e.getKey() + " = " + e.getValue())
				.collect(Collectors.joining("\n"));
		logger.log(Level.WARN, str);
		return str;
	}
}
