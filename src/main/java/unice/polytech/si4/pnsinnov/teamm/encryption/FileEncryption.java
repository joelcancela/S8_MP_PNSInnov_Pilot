package unice.polytech.si4.pnsinnov.teamm.encryption;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by Nassim B on 5/28/18.
 */
@Path("fileencryption")
public class FileEncryption {
	private static final Logger logger = LogManager.getLogger(FileEncryption.class);
	private final String CIPHER_ALGO = "AES";
	private static final String KEY_ALGORITHM = "AES";

	@QueryParam("fileid") String fileid;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	public void getIt(@Context HttpServletRequest request, @Context HttpServletResponse response) {

		if(fileid.isEmpty() || fileid == null) {
			request.setAttribute("error", "A file id must be provided");
			try {
				//FIXME : Multiple User
				//request.setAttribute("ownFile", googleDrive.classifyFiles());
				request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
			} catch (IOException | ServletException e) {
				e.printStackTrace();
			}
		}

		String downloadedPath = null;
		//FIXME : Multiple User
		/*try {
			//downloadedPath = Login.googleDrive.downloadFile(false, fileid, null); //TODO : Currently exportedMime is
			// mocked in method, must be provided by gui
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		logger.log(Level.INFO, "File downloaded to " + downloadedPath);
		File file = new File(downloadedPath);
		StringBuilder stringBuilder = new StringBuilder();


		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
		} catch (FileNotFoundException e) {
			logger.log(Level.ERROR, e.getMessage());
			logger.log(Level.INFO,"##### JPNE #####");
			//logger.log(Level.INFO,fileurl);
		}


		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, KeyGeneration.getKey());


			FileInputStream inputStream = new FileInputStream(file);
			byte[] inputBytes = new byte[(int) file.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);

			File outFile = new File(file.getPath()+"-crypted");
			FileOutputStream outputStream = new FileOutputStream(outFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

			//FIXME : Multiple User
			//Login.googleDrive.uploadFile(false, outFile);

			//return Base64.encodeBase64URLSafeString(cipher.doFinal(stringBuilder.toString().getBytes());
			//return "File crypted and named : " + file.getName() + "-crypted";
			request.setAttribute("success", "the file " + file.getName() + " has crypted and uploaded as : " + file.getName() + "-crypted");

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | IOException e) {
			logger.log(Level.ERROR, e.getMessage());
			request.setAttribute("error", "An error occured while crypting the file " + file.getName());
		}

		try {
			//FIXME : Multiple User
			//request.setAttribute("ownFile", googleDrive.classifyFiles());
			request.getRequestDispatcher("/gdrive-list.jsp").forward(request, response);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}

}
