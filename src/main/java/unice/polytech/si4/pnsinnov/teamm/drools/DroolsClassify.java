package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.List;

@Path("drools")
public class DroolsClassify {
    @POST
    public void classifyFiles(@Context HttpServletRequest request,
                              @Context HttpServletResponse response) throws IOException, ServletException {
        List<File> files = Login.googleDrive.classifyFiles();
        new ProxyGoogleDrive().applyRules(files);
        request.setAttribute("list", files);
        request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
    }
}
