package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Path("drools")
public class DroolsClassify {
    @POST
    public void classifyFiles(@Context HttpServletRequest request,
                              @Context HttpServletResponse response,
                              @QueryParam("userId") String userID) throws IOException, ServletException {
        List<File> files = ((GDriveSession)Login.storageSessions.get(userID)).getDrive().getAutomaticFilesList();
        System.out.println("PASSING FILES : " + files.stream().map(file -> file.getName()).collect(Collectors.toList()));
        new ProxyGoogleDrive().applyRules(files, userID);
        request.setAttribute("list", files);
        request.setAttribute("ownFile", ((GDriveSession)Login.storageSessions.get(userID)).getDrive().classifyFiles());
        request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
    }
}
