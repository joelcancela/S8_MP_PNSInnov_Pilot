package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.IOException;
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
	String redirectURL = "https://" + ConfigurationLoader.getInstance().getHost() + "/GDriveOAuth";

	public GoogleAuthorizationCodeRequestUrl getAuthRequest() {
		return flow.newAuthorizationUrl().setRedirectUri(redirectURL);
	}

	public void setCredential(String code) throws IOException {//NOT SURE TODO:
		logger.log(Level.INFO, "AuthCode: " + code);
		logger.log(Level.INFO, "Flow: " + flow.toString());
		GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code);
		logger.log(Level.INFO, "TOKEN RQST: " + tokenRequest);
		logger.log(Level.INFO, "REDIRECT URL: " + redirectURL);
		tokenRequest = tokenRequest.setRedirectUri(redirectURL);
		logger.log(Level.INFO, "TOKEN RQST: " + tokenRequest);
		TokenResponse response = tokenRequest.execute();
		logger.log(Level.INFO, "TOKEN RESPONSE: " + response);
		logger.log(Level.INFO, "USERID: " + userID);
		credential = this.flow.createAndStoreCredential(response, userID);//FIXME: NPE ON RESPONSE
	}

	public void setFlow(GoogleAuthorizationCodeFlow flow) {
		this.flow = flow;
	}
}
