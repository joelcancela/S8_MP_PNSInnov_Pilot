package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.http.FileContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;

/**
 * Class FileUploader that allows to update files to different types of drives
 * TODO: To edit when abstraction will be done
 */
@Path("upload")
public class FileUploader {

	private static final Logger logger = LogManager.getLogger(FileUploader.class);

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public void importFile(@Context HttpServletRequest request,
	                       @Context HttpServletResponse response,
	                       @FormDataParam("inputFile") InputStream fileInputStream,
	                       @FormDataParam("inputFile") FormDataContentDisposition fileDetail,
	                       @FormDataParam("inputFile") FormDataBodyPart bodyPart,
	                       @FormDataParam("encrypt") String encrypt
	) {
		GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
		File uploadedFile = writeToFile(fileInputStream, fileDetail.getFileName());
		com.google.api.services.drive.model.File gFile = new com.google.api.services.drive.model.File();
		FileContent gFileContent = new FileContent(bodyPart.getMediaType().toString(), uploadedFile);
		gFile.setName(fileDetail.getFileName());
		try {
			com.google.api.services.drive.model.File file;
			file = session.getDrive().files().create(gFile, gFileContent).setFields("id").execute();
			logger.log(Level.INFO, "File ID : " + file.getId());
			if (encrypt != null && encrypt.equals("on")) {
				response.sendRedirect("fileencryption?fileid=" + file.getId());
			} else {
				response.sendRedirect("drive-list");
			}
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		logger.log(Level.INFO, encrypt);
	}

	// save uploaded file to new location
	private File writeToFile(InputStream uploadedInputStream,
	                         String uploadedFileLocation) {

		try {
			OutputStream out;
			int read;
			byte[] bytes = new byte[1024];

			File file = new File(uploadedFileLocation);
			out = new FileOutputStream(file);
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			return file;
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return null;
	}

}
