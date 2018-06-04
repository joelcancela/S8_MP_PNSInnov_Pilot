package unice.polytech.si4.pnsinnov.teamm.api;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.*;

@Path("import")
public class ImportFile {

    private static final Logger logger = LogManager.getLogger(ImportFile.class);

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void importFile(
            @FormDataParam("inputFile") InputStream fileInputStream,
            @FormDataParam("inputFile") FormDataContentDisposition fileDetail,
            @FormParam("encrypt") String encrypt
    ) {

        logger.log(Level.INFO, writeToFile(fileInputStream, "uploadedFile"));
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
            while ((read = uploadedInputStream.read(bytes)) != - 1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
