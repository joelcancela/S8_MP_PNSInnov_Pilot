package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("ruleCreation")
public class FindFolders {
    @GET
    public void getFolders(@Context HttpServletRequest request,
                                   @Context HttpServletResponse response) throws ServletException, IOException {
        List<String> folderNames = new ArrayList<>();
        try {
            List<File> allFiles = GDrive.getGDrive().getFilesList(Login.retrieveDriveSessionFromCookie(request));
            for (File file : allFiles) {
                if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                    folderNames.add(file.getName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.setAttribute("listFolders", folderNames);
        request.getRequestDispatcher("/rules-creation.jsp").forward(request, response);
    }
}
