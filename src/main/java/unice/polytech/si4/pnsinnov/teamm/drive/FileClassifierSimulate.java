package unice.polytech.si4.pnsinnov.teamm.drive;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.Metadata;
import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.compiler.lang.dsl.DSLMappingEntry;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledExtensionRException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledMimeTypeRException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application.FileClassifierDrive;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application.FileClassifierDropBox;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application.FileClassifierGoogleDrive;

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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Nassim B on 6/4/18.
 */
@Path("drools-simulate")
public class FileClassifierSimulate {

    private static final Logger logger = LogManager.getLogger(FileClassifierSimulate.class);

    @POST
    public Response classifyFilesSimulation(@Context HttpServletRequest request,
                                            @Context HttpServletResponse response,
                                            @QueryParam("drive") String driveType) {
        Map<String, Object> map = new HashMap<>();
        if (driveType.equals("gdrive")) {
            GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
            List<File> files = GDrive.getGDrive().getAutomaticFilesList(session);
            logger.log(Level.DEBUG, "PASSING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors
                    .toList()));
            FileRepresentation tree = new FileClassifierGoogleDrive().applyRules(files, session, Login.retrieverUserIDFromCookie(request), true);
            try {
                map.put("ownFile", GDrive.getGDrive().buildFileTree(session));
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("treeSimulation", tree);
            map.put("simulate", true);

            return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
        } else {
            DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
            try {
                List<Metadata> files = DropboxDrive.getDropboxDrive().getFilesList(session, "/_Automatic");
                FileRepresentation tree = new FileClassifierDropBox().applyRules(files, session, Login.retrieverUserIDFromCookie(request), true);
                map.put("ownFile", DropboxDrive.getDropboxDrive().buildFileTree(session));
                map.put("treeSimulation", tree);
                map.put("simulate", true);
            } catch (DbxException e) {
                e.printStackTrace();
            }
            return Response.ok(new Viewable("/gdrive-list.jsp", map)).build();
        }
    }
}
