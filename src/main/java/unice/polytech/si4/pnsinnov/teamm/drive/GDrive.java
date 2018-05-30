package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
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
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class GDrive
 *
 * @author Joël CANCELA VAZ
 */
public class GDrive {
	private final Logger logger = Logger.getLogger(GDrive.class.getName());
	private final File DATA_STORE_DIR = new File("target/store");
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final String APPLICATION_NAME = "PrivateMemo";
	private FileDataStoreFactory dataStoreFactory;
	private HttpTransport httpTransport;
	private Drive drive;
	private Credential credential;

	public void initialize() {
		credential = Login.gDriveSession.credential;
		drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
	}

	public GDrive() throws IOException, GeneralSecurityException {
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GDrive.class.getResourceAsStream("/client_secrets.json")));
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
				.build();
		Login.gDriveSession.setFlow(flow);
	}

	public List<com.google.api.services.drive.model.File> getFilesList() throws IOException {
		Drive.Files.List request = drive.files().list().setFields("nextPageToken, files");
		List<com.google.api.services.drive.model.File> files = new ArrayList<>(request.execute().getFiles());
		if (files.isEmpty()) {
			logger.log(Level.INFO, "No files found.");
		} else {
			logger.log(Level.INFO, files.size() + " Files found.");
		}
		return files;
	}

	public Channel subscribeToChanges() {//TODO: watch mutliples sessions
		Channel notifications = watchChange(drive, "userID", "https://" + ConfigurationLoader.getInstance().getHost() + "/notifications");
		logger.log(Level.INFO, "Watching for changes on Google Drive");
		return notifications;
	}

	private Channel watchChange(Drive service, String channelId,
	                            String channelAddress) {
		Channel channel = new Channel();
		channel.setId(channelId);
		channel.setType("web_hook");
		channel.setAddress(channelAddress);
		try {
			return service.changes().watch(service.changes().getStartPageToken().execute().getStartPageToken(), channel).execute();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	public void unsubscribeChannel(Channel channel) throws IOException {
		drive.channels().stop(channel);
	}

	public void getChanges() throws IOException {//FIXME: When the notifications will be triggered by POST
		for (int i = 0; i < 10; i++) {
			String pageToken = drive.changes().getStartPageToken().execute().getStartPageToken();
			List<Change> changes = drive.changes().list(pageToken)
					.execute().getChanges();
			System.out.println(changes);
		}
	}
}
