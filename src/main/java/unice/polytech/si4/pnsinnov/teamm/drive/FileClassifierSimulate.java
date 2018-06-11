package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledExtensionRException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledMimeTypeRException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application.FileClassifierDrive;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application.FileClassifierGoogleDrive;

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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Nassim B on 6/4/18.
 */
@Path("drools-simulate")
public class FileClassifierSimulate {

	private static final Logger logger = LogManager.getLogger(FileClassifierSimulate.class);

	@POST
	public Response classifyFiles(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

		List<File> files = GDrive.getGDrive().getAutomaticFilesList(session);
		logger.log(Level.DEBUG, "PASSING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors
				.toList()));
		// new FileClassifierGoogleDrive().applyRules(files, session, Login.retrieverUserIDFromCookie(request));

		Map<String, Object> map = new HashMap();
		try {
			map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}

		return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
	}
}
