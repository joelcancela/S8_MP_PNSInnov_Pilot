package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO: To edit when abstraction will be done
 */
@Path("drools")
public class FileClassifierDrive {

	private static final Logger logger = LogManager.getLogger(FileClassifierDrive.class);

	@POST
	public Response classifyFiles(@Context HttpServletRequest request,
	                          @Context HttpServletResponse response) throws IOException, ServletException {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		List<File> files = GDrive.getGDrive().getAutomaticFilesList(session);
		logger.log(Level.DEBUG, "PASSING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors
				.toList()));
		new FileClassifierGoogleDrive().applyRules(files, session, Login.retrieverUserIDFromCookie(request));

		Map<String, Object> map = new HashMap();
		try {
			map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}

		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
	}
}
