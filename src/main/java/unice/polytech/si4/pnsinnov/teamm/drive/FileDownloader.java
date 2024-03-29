package unice.polytech.si4.pnsinnov.teamm.drive;

import com.dropbox.core.DbxException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.dropbox.DropboxSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Nassim B on 5/30/18.
 * TODO: To edit when abstraction will be done
 */
@Path("downloadDrive")
public class FileDownloader {
    private static final Logger logger = LogManager.getLogger(FileDownloader.class);

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response, @QueryParam("fileid") String fileid, @QueryParam("drive") String drive) {

        if (fileid == null) {
            logger.log(Level.ERROR, "A file id must be provided");
            return Response.status(404).build();
        }
        InputStream out = null;
        String filename = null;
        if (drive.equals("gdrive")) {
            GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
            try {
                filename = GDrive.getGDrive().getFileName(session, fileid);
                out = GDrive.getGDrive().downloadFileDirect(session, fileid);
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        } else if (drive.equals("dropbox")) {
            DropboxSession session = Login.retrieveDropboxSessionFromCookie(request);
            try {
                filename = DropboxDrive.getDropboxDrive().getFileName(session, fileid);
                out = DropboxDrive.getDropboxDrive().downloadFileDirect(session, fileid);
            } catch (DbxException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }

        return Response.ok(out).header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .build();
    }
}
