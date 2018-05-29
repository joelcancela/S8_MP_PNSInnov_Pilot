package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.IOException;

/**
 * Class GDriveSession
 *
 * @author Joël CANCELA VAZ
 */
public class GDriveSession {
	GoogleAuthorizationCodeFlow flow;
	Credential credential;
	String userID;
	String redirectURL = "https://www."+ConfigurationLoader.getInstance().getHost() + "/GDriveOAuth";

	public GoogleAuthorizationCodeRequestUrl getAuthRequest() {
		return flow.newAuthorizationUrl().setRedirectUri(redirectURL);
	}

	public void setCredential(String code) throws IOException {//NOT SURE TODO:
		TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURL).execute();
		credential = this.flow.createAndStoreCredential(response, userID);
	}

	public void setFlow(GoogleAuthorizationCodeFlow flow) {
		this.flow = flow;
	}
}
