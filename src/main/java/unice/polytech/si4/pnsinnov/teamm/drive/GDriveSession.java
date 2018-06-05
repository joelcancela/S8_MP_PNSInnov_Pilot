package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import org.apache.http.client.methods.HttpTrace;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class GDriveSession
 *
 * @author Joël CANCELA VAZ
 */
public class GDriveSession {
	private final Logger logger = Logger.getLogger(GDriveSession.class.getName());
	GoogleAuthorizationCodeFlow flow;
	Credential credential;
	String userID;
	private Drive drive;
	String redirectURLOAuth = ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/GDriveOAuth";
	private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private final String APPLICATION_NAME = "PrivateMemo";
	public String savedStartPageToken;

	public GDriveSession(String userID, GoogleAuthorizationCodeFlow flow) {
		this.userID = userID;
		this.flow = flow;
	}

	public GoogleAuthorizationCodeRequestUrl getAuthRequest() {
		return flow.newAuthorizationUrl().setRedirectUri(redirectURLOAuth);
	}

	public void setCredential(String code) throws IOException, GeneralSecurityException {
		GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
		tokenRequest = tokenRequest.setRedirectUri(redirectURLOAuth);
		TokenResponse response = tokenRequest.execute();
		credential = this.flow.createAndStoreCredential(response, userID);

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
		try {
			savedStartPageToken = drive.changes()
					.getStartPageToken().execute().getStartPageToken();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public Drive getDrive() {
		return drive;
	}
}
