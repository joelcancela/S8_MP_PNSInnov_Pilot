package unice.polytech.si4.pnsinnov.teamm;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.Channel;
import unice.polytech.si4.pnsinnov.teamm.misc.ConfigurationLoader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

	private String host = ConfigurationLoader.getInstance().getHost();
	private static final File DATA_STORE_DIR = new File("target/store");
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String APPLICATION_NAME = "PrivateMemo";
	private static FileDataStoreFactory dataStoreFactory;
	private static HttpTransport httpTransport;
	private static Drive drive;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String getIt() {
//        return "Got it!";
//    }
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() throws IOException {
		System.out.println(host);
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			Credential credential = authorize();
			// set up the global Drive instance
			drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
					APPLICATION_NAME).build();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Drive.Files.List request = drive.files().list().setFields("nextPageToken, files");
			List<com.google.api.services.drive.model.File> files = request.execute().getFiles();
			if (files == null || files.size() == 0) {
				System.out.println("No files found.");
			} else {
				System.out.println("Files:");
				for (com.google.api.services.drive.model.File file : files) {
					System.out.printf("%s (%s)\n", file.getName(), file.getId());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Channel notifications = watchChange(drive,"MyDrive","web_hook","http://"+host+"/PrivateMemo/api/notifications");
		Channel notifications = watchChange(drive, UUID.randomUUID().toString(), "web_hook", "https://www.bounouas.com/notifications", drive
				.changes().getStartPageToken());
		System.out.println("Watching repo");
		for (int i = 0; i < 10; i++) {
			String pageToken = drive.changes().getStartPageToken().execute().getStartPageToken();
			List<Change> changes = drive.changes().list(pageToken)
					.execute().getChanges();
			System.out.println(changes);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		//TODO: Resource file
		return "Got it!";
	}

	private static Channel watchChange(Drive service, String channelId,
	                                   String channelType, String channelAddress, Drive.Changes.GetStartPageToken pageToken) {
		Channel channel = new Channel();
		channel.setId(channelId);
		channel.setType(channelType);
		channel.setAddress(channelAddress);
		try {
			return service.changes().watch(service.changes().getStartPageToken().execute().getStartPageToken(), channel).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static Credential authorize() throws Exception {
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(MyResource.class.getResourceAsStream("/client_secrets.json")));
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
				.build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
}
