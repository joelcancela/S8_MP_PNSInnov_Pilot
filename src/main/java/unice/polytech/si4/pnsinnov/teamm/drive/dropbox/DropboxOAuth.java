package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

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

/**
 * Class DropboxOAuth
 *
 * @author Joël CANCELA VAZ
 */
@Path("DropboxOAuth")
public class DropboxOAuth {
	private static final Logger logger = LogManager.getLogger(DropboxOAuth.class.getName());

	@GET
	public void receiveCodeDropBox(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response,
	                               @QueryParam("code") String code,
	                               @QueryParam("state") String state) throws
			IOException {
		String userId = request.getSession().getAttribute("user.logged_dropbox").toString();

		if (userId == null) {
			throw new RuntimeException("User ID can't be retrieved from cookies");
		}

		DropboxSession dropboxSession = Login.getDropboxSession(userId);
		dropboxSession.setCode(code);

		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.sendRedirect("drive-list?drive=dropbox");
	}
}
