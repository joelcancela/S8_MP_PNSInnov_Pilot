package unice.polytech.si4.pnsinnov.teamm.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

/**
 * Created by Nassim B on 6/5/18.
 */
@Path("logout")
public class Logout {

    private static final Logger logger = LogManager.getLogger(Logout.class);

    @GET
    public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        GDriveSession session = Login.retrieveDriveSessionFromCookie(request);

        if (session == null) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        } else {
            Cookie logoutCookie = new Cookie("userID", "");
            logoutCookie.setMaxAge(0);
            response.addCookie(logoutCookie);
            try {
                response.sendRedirect("/PrivateMemo");
            } catch (IOException e) {
                try {
                    logger.log(Level.ERROR, "Error while redirecting to home"); //TODO : Handle this case properly
                    response.sendRedirect("connection-failed");
                } catch (IOException e1) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        }
    }
}
