package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Path("drools")
public class DroolsClassify {

	private static final Logger logger = LogManager.getLogger(DroolsClassify.class);

	@POST
	public void classifyFiles(@Context HttpServletRequest request,
	                          @Context HttpServletResponse response) throws IOException, ServletException {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
		if (session == null) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		} else {
			List<File> files = GDrive.getGDrive().getAutomaticFilesList(session);
			logger.log(Level.DEBUG, "PASSING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors
					.toList()));
			new ProxyGoogleDrive().applyRules(files, session);
			request.setAttribute("list", files);
			request.setAttribute("ownFile", GDrive.getGDrive().classifyFiles(session));
			request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
		}
	}
}
