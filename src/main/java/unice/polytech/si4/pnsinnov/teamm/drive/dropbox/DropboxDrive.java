package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;

import java.io.File;

/**
 * Class DropboxDrive
 *
 * @author Joël CANCELA VAZ
 */
public class DropboxDrive {

	private static Logger logger = LogManager.getLogger(DropboxDrive.class);
	private DbxAppInfo appInfo = null;
	private static DropboxDrive INSTANCE;

	public DropboxDrive() {

		try {
			String secretsPath = getClass().getResource("/dropbox_secrets.json").toString().replace("file:/", "");
			File dropboxSecret = new File(secretsPath);
			logger.log(Level.ERROR, secretsPath);
			logger.log(Level.ERROR, secretsPath);
			logger.log(Level.ERROR, secretsPath);
			logger.log(Level.ERROR, secretsPath);
			logger.log(Level.ERROR, secretsPath);
			appInfo = DbxAppInfo.Reader.readFromFile(dropboxSecret);
			logger.log(Level.DEBUG, appInfo.toString());
		} catch (com.dropbox.core.json.JsonReader.FileLoadException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		logger.log(Level.DEBUG, appInfo.toString());
		logger.log(Level.DEBUG, appInfo.toString());
		logger.log(Level.DEBUG, appInfo.toString());
		logger.log(Level.DEBUG, appInfo.getHost());
		logger.log(Level.DEBUG, appInfo.getKey());
		logger.log(Level.DEBUG, appInfo.getSecret());
		logger.log(Level.DEBUG, appInfo.toString());
		// Run through Dropbox API authorization process
	}

	public static DropboxDrive getDropboxDrive() {
		if (INSTANCE == null) {
			INSTANCE = new DropboxDrive();
		}
		return INSTANCE;
	}

	public DropboxSession createNewSession(String userid) {
		DbxRequestConfig requestConfig = new DbxRequestConfig(userid);//FIXME: to change
		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo);
		return new DropboxSession(requestConfig, webAuth, appInfo, userid);
	}

	public FileRepresentation buildFileTree(DropboxSession session) {
		return null; //TODO
	}
}
