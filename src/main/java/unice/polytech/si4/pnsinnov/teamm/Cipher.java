package unice.polytech.si4.pnsinnov.teamm;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Nassim B on 5/28/18.
 */
@Path("mycipher")
public class Cipher {

	private static final File DATA_STORE_DIR = new File("target/store");
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		String fileurl = Cipher.class.getResource("/tocrypt.txt").getFile();
		File file = new File(fileurl);
		StringBuilder stringBuilder = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				stringBuilder.append(scanner.next());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("##### JPNE #####");
			System.out.println(fileurl);
		}
		return stringBuilder.toString();
	}
}
