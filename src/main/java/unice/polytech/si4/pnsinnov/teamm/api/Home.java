package unice.polytech.si4.pnsinnov.teamm.api;

import org.glassfish.jersey.server.mvc.Viewable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Class Home that displays the home page
 *
 * @author Joël CANCELA VAZ
 */
@Path("")
public class Home {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getHomepage(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		return Response.ok(new Viewable("/index.jsp", null)).build();
	}
}