package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("ruleCreation")
public class FindFolders {
    private static final Logger logger = LogManager.getLogger(FindFolders.class);

    @GET
    public Response getFolders(@Context HttpServletRequest request,
                                   @Context HttpServletResponse response) {
        List<String> folderNames = new ArrayList<>();
        Map<String, Object> map = new HashMap();

        try {
            GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
            List<File> allFiles = GDrive.getGDrive().getFilesList(session);
            createFoldersList(folderNames, allFiles);
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        map.put("listFolders", folderNames);

        return Response.ok(new Viewable("/rules-creation.jsp", map)).build(); // TODO : Create a dedicated page to key retrieving
    }

    void createFoldersList(List <String> folderNames, List <File> allFiles) {
        for (File file : allFiles) {
            if(file.getMimeType().equals("application/vnd.google-apps.folder")
                    && !file.getTrashed()
                    && !file.getName().equals("_NoRuleApplied")
                    && !file.getName().equals("_Automatic")){
                folderNames.add(file.getName());
            }
        }
    }
}
