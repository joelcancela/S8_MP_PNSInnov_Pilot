package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;
import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
                                  @Context HttpServletResponse response,
                                  @QueryParam("drive") String driveType) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        if (driveType.equals("gdrive")) {
            GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
            List<File> files = GDrive.getGDrive().getAutomaticFilesList(session);
            logger.log(Level.DEBUG, "PASSING FILES : " + files.stream().map(File::getName).collect(Collectors
                    .toList()));
            new FileClassifierGoogleDrive().applyRules(files, session, Login.retrieverUserIDFromCookie(request), false);
            try {
                map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        } else {
            DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
            List<Metadata> files = null;
            try {
                files = DropboxDrive.getDropboxDrive().getFilesList(session, "/_Automatic");
            } catch (DbxException e) {
                e.printStackTrace();
            }
            new FileClassifierDropBox().applyRules(files, session, Login.retrieverUserIDFromCookie(request), false);
            map.put("ownFile", DropboxDrive.getDropboxDrive().buildFileTree(session));
        }
        return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
    }
}
