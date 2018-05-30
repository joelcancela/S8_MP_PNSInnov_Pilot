package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.IOException;
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
	String redirectURLOAuth = ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/GDriveOAuth";

	public GDriveSession(String userID) {
		this.userID = userID;
	}

	public GoogleAuthorizationCodeRequestUrl getAuthRequest() {
		return flow.newAuthorizationUrl().setRedirectUri(redirectURLOAuth);
	}

	public void setCredential(String code) throws IOException {
		GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
		tokenRequest = tokenRequest.setRedirectUri(redirectURLOAuth);
		TokenResponse response = tokenRequest.execute();
		credential = this.flow.createAndStoreCredential(response, userID);
	}

	public void setFlow(GoogleAuthorizationCodeFlow flow) {
		this.flow = flow;
	}

	public Credential getCredential() {
		return credential;
	}
}
