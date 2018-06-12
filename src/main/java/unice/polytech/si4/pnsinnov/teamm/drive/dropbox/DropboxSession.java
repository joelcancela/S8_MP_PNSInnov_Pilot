package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Class DropboxSession
 *
 * @author Joël CANCELA VAZ
 */
public class DropboxSession {
	private DbxAppInfo appInfo;
	private DbxWebAuth webAuth;
	private DbxRequestConfig dbxRequestConfig;
	private String userid;
	private String redirectURI = ConfigurationLoader.getInstance().getHost() + "/Pilot/api/DropboxOAuth";
	private DbxClientV2 dbxClientV2;

	public DropboxSession(DbxRequestConfig requestConfig, DbxWebAuth webAuth, DbxAppInfo appInfo, String userid) {
		this.dbxRequestConfig = requestConfig;
		this.webAuth = webAuth;
		this.appInfo = appInfo;
		this.userid = userid;
	}

	public String getAuthURL(HttpServletRequest request) {
		DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
				.withRedirectUri(redirectURI, new
						DbxStandardSessionStore(request.getSession(), userid))//TODO:
				.build();
		return webAuth.authorize(webAuthRequest);
	}

	public void setCode(String code) {
		DbxAuthFinish authFinish;
		try {
			authFinish = webAuth.finishFromCode(code.trim(), redirectURI);
		} catch (DbxException ex) {
			System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
			return;
		}

		System.out.println("Authorization complete.");
		System.out.println("- User ID: " + authFinish.getUserId());
		System.out.println("- Account ID: " + authFinish.getAccountId());
		System.out.println("- Access Token: " + authFinish.getAccessToken());

		// Save auth information to output file.
		DbxAuthInfo authInfo = new DbxAuthInfo(authFinish.getAccessToken(), appInfo.getHost());
		String tokenPath = Paths.get("").toAbsolutePath().normalize().toString() + "/target/store/";
		File storeFolder = new File(tokenPath);
		storeFolder.mkdir();
		String tokenName = userid + "_dropbox";
		File output = new File(tokenPath + tokenName);
		try {
			DbxAuthInfo.Writer.writeToFile(authInfo, output);
			System.out.println("Saved authorization information to \"" + output.getCanonicalPath() + "\".");
		} catch (IOException ex) {
			System.err.println("Error saving to <auth-file-out>: " + ex.getMessage());
			System.err.println("Dumping to stderr instead:");
			try {
				DbxAuthInfo.Writer.writeToStream(authInfo, System.err);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
		dbxClientV2 = new DbxClientV2(dbxRequestConfig, authFinish.getAccessToken());

	}

	public DbxClientV2 getDropboxClient() {
		return dbxClientV2;
	}
}
