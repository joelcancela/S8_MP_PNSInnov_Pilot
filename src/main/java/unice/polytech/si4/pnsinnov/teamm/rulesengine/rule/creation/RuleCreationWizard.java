package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.creation;

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

@Path("ruleCreation")
public class RuleCreationWizard {
    private static final Logger logger = LogManager.getLogger(RuleCreationWizard.class);

    @GET
    public Response getFolders(@Context HttpServletRequest request,
                               @Context HttpServletResponse response,
                               @QueryParam("drive") String driveType) {
        List <String> folderNames = new ArrayList <>();
        Map <String, Object> map = new HashMap();
        map.put("drive", driveType);
        try {
            if (driveType.equals("gdrive")) {
                GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
                List <File> allFiles = GDrive.getGDrive().getFilesList(session);
                createFoldersListGoogle(folderNames, allFiles);
            } else if (driveType.equals("dropbox")) {
                DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
                List <Metadata> allFiles = DropboxDrive.getDropboxDrive().getFilesList(session, "");
                createFoldersListDropbox(folderNames, allFiles);
            }

        } catch (IOException | DbxException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        map.put("listFolders", folderNames);

        return Response.ok(new Viewable("/rules-creation.jsp", map)).build(); // TODO : Create a dedicated page to key retrieving
    }

    public void createFoldersListGoogle(List <String> folderNames, List <File> allFiles) {
        for (File file : allFiles) {
            if (file.getMimeType().equals("application/vnd.google-apps.folder")
                    && ! file.getTrashed()
                    && ! file.getName().equals("_NoRuleApplied")
                    && ! file.getName().equals("_Automatic")) {
                folderNames.add(file.getName());
            }
        }
    }

    public void createFoldersListDropbox(List <String> folderNames, List <Metadata> allFiles) {
        for (Metadata file : allFiles) {
            if (! file.getName().equals("_NoRuleApplied") && ! file.getName().equals("_Automatic")) {
                folderNames.add(file.getName());
            }
        }
    }
}
