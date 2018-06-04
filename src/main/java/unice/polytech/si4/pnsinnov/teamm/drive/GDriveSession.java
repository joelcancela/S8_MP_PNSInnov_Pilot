package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import unice.polytech.si4.pnsinnov.teamm.api.StorageSession;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class GDriveSession
 *
 * @author Joël CANCELA VAZ
 */
public class GDriveSession extends StorageSession {
	private final Logger logger = Logger.getLogger(GDriveSession.class.getName());
	private static GoogleAuthorizationCodeFlow flow;
	private FileDataStoreFactory dataStoreFactory;
	private GDrive gdrive;

	Credential credential;
	String userID;
	private final String APPLICATION_NAME = "PrivateMemo";
	private HttpTransport httpTransport;
	private String savedStartPageToken;
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final File DATA_STORE_DIR = new File("target/store");

	String redirectURLOAuth = ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/GDriveOAuth";

	public GDriveSession(String userID) {
		this.userID = userID;

		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			logger.log(Level.SEVERE,"Error occured while establishing http transport");
		}

		try {
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
					new InputStreamReader(GDrive.class.getResourceAsStream("/client_secrets.json")));

			if (GDriveSession.flow == null) {
				flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
						Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
						.build();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GoogleAuthorizationCodeRequestUrl getAuthRequest() {
		return flow.newAuthorizationUrl().setRedirectUri(redirectURLOAuth);
	}

	public void setCredential(String code) throws IOException {
		GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
		tokenRequest = tokenRequest.setRedirectUri(redirectURLOAuth + "&userId=" + this.userID);
		TokenResponse response = tokenRequest.execute();
		credential = this.flow.createAndStoreCredential(response, userID);
	}

	public void setFlow(GoogleAuthorizationCodeFlow flow) {
		this.flow = flow;
	}

	public Credential getCredential() {
		return credential;
	}

	public void initialize() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			logger.log(Level.SEVERE,"Error occured while establishing http transport");
		}

		Drive drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
		try {
			savedStartPageToken = drive.changes()
					.getStartPageToken().execute().getStartPageToken();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}

		this.gdrive = new GDrive(drive);
	}


	public GDrive getDrive() {
		return gdrive;
	}


}
