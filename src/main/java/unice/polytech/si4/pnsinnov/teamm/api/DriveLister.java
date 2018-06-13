package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class DriveLister that displays the list of the files in a drive
 *
 * @author Nassim Bounouas
 * TODO: To edit when abstraction will be done
 */
@Path("drive-list")
public class DriveLister {
	private static final Logger logger = LogManager.getLogger(DriveLister.class.getName());

	@GET
	public Response getPage(@Context HttpServletRequest request, @Context HttpServletResponse response,
	                        @QueryParam("drive") String driveType) {
		Map<String, Object> map = new HashMap<>();
		if (driveType.equals("gdrive")) {
			GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
			List<String> createdFolders = checkNeededFolders(session);
			if (!createdFolders.isEmpty()){
				map.put("createdFolders", String.join(" and ", createdFolders));
			}
			try {
				logger.log(Level.INFO, "session is : " + session);
				map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage());
			}

			return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
		} else if (driveType.equals("dropbox")) {
			DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
			logger.log(Level.INFO, "session is : " + session);
			map.put("fileRepresentation", DropboxDrive.getDropboxDrive().buildFileTree(session));
			return Response.ok(new Viewable("/dropbox-list.jsp", map)).build();
		}
		return Response.status(404).build();

	}

	private List<String> checkNeededFolders(GDriveSession session) {
		boolean noRuleAppliedCreated = createFolderIfNecessary("_NoRuleApplied", session);
		boolean automaticCreated = createFolderIfNecessary("_Automatic", session);
		List<String> result = new ArrayList<>();
		if (noRuleAppliedCreated) result.add("_NoRuleApplied");
		if (automaticCreated) result.add("_Automatic");
		return result;
	}

	private boolean createFolderIfNecessary(String name, GDriveSession session) {
		try {
			List<File> files = session.getDrive().files().list()
					.setQ("mimeType='application/vnd.google-apps.folder' and trashed=false and name='" + name + "'")
					.execute().getFiles();
			if (files.isEmpty()) {
				File fileMetaData = new File();
				fileMetaData.setName(name);
				fileMetaData.setMimeType("application/vnd.google-apps.folder");
				try {
					session.getDrive().files().create(fileMetaData).setFields("id").execute();
					return true;
				} catch (IOException e) {
					logger.log(Level.ERROR, e.getMessage());
				}
			}
			return false;
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return false;
	}
}
