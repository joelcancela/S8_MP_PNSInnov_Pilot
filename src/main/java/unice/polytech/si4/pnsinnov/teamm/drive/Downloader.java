package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Nassim B on 5/30/18.
 */
@Path("downloadDrive")
public class Downloader {
	private static final Logger logger = LogManager.getLogger(Downloader.class);

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response get(@QueryParam("fileid") String fileid) {
		if (fileid == null) {
			logger.log(Level.ERROR, "A file id must be provided");
			return Response.status(404).build();
		}
		InputStream out = null;
		String filename = null;
		//FIXME : Multiple User
		/*try {
			filename = Login.googleDrive.getFileName(fileid);
			//out = Login.googleDrive.downloadFileDirect(fileid);
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}*/

		return Response.ok(out).header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
				.build();
	}
}
