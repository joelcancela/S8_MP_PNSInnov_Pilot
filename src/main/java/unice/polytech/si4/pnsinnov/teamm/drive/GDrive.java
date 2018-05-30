package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.Channel;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
	public Drive drive;
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
		Channel notifications = watchChange(drive, "skynet-id-00", ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/notifications");
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

	public void getChanges() throws IOException {
		String pageToken = drive.changes().getStartPageToken().execute().getStartPageToken();
		List<Change> changes = drive.changes().list(pageToken)
				.execute().getChanges();
		logger.log(Level.INFO, changes.toString());
	}

	/**
	 * TODO: To try and test
	 * Uploads a file using either resumable or direct media upload.
	 */
	public com.google.api.services.drive.model.File uploadFile(boolean useDirectUpload, File file) throws IOException {
		com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
		fileMetadata.setName(file.getName());
		FileContent mediaContent = new FileContent("", file);//FIXME 1st arg: Type Mime
		Drive.Files.Create insert = drive.files().create(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}

	/**
	 * TODO: To try and test
	 * Downloads a file using either resumable or direct media download.
	 */
	public void downloadFile(boolean useDirectDownload, com.google.api.services.drive.model.File uploadedFile)
			throws IOException {
		// create parent directory (if necessary)
		File parentDir = new File("/downloads/userID");
		if (!parentDir.exists() && !parentDir.mkdirs()) {
			throw new IOException("Unable to create parent directory");
		}
		OutputStream out = new FileOutputStream(new File(parentDir, uploadedFile.getName()));

		MediaHttpDownloader downloader =
				new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
		downloader.setDirectDownloadEnabled(useDirectDownload);
		downloader.download(new GenericUrl(uploadedFile.getWebContentLink()), out);
	}

	public OwnFile classifyFiles() throws IOException {
        com.google.api.services.drive.model.File rootFile = drive.files().get("root").setFields("id").execute();
		List<OwnFile> folders = new ArrayList<>();
		List<OwnFile> files = new ArrayList<>();
		OwnFile root = new OwnFile(rootFile);
		folders.add(root);
		List<com.google.api.services.drive.model.File> filesDrive = getFilesList();
		for (com.google.api.services.drive.model.File file : filesDrive) {
		    if (file.getParents() != null) {
                if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                    folders.add(new OwnFile(file));
                } else {
                    files.add(new OwnFile(file));
                }
            }
		}
        for (OwnFile child : folders) {
            for (OwnFile possibleParent : folders) {
                if (possibleParent.file.getId().equals(child.file.getParents().get(0))){
                    possibleParent.addFolder(child);
                    break;
                }
            }
        }
        for (OwnFile child : files) {
            for (OwnFile possibleParent : folders) {
                if (possibleParent.file.getId().equals(child.file.getParents().get(0))){
                    possibleParent.addFile(child);
                    break;
                }
            }
        }
		return root;
	}
}
