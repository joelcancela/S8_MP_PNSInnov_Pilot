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
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.FileList;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private String savedStartPageToken;

	public void initialize() {
		credential = Login.gDriveSession.credential;
		drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
				APPLICATION_NAME).build();
		try {
			savedStartPageToken = drive.changes()
					.getStartPageToken().execute().getStartPageToken();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
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

    public List<com.google.api.services.drive.model.File> getAutomaticFilesList() {
        List<com.google.api.services.drive.model.File> automaticFiles = new ArrayList<>();
        String folderId = null;
        FileList folders = null;
        try {
            folders = drive.files().list().setQ("mimeType='application/vnd.google-apps.folder' and name = 'automatic'").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (com.google.api.services.drive.model.File f : folders.getFiles()) {
            if (f.getName().equals("automatic")) {
                folderId = f.getId();
            }
        }
        try {
            List<com.google.api.services.drive.model.File> files = getFilesList();
            for (com.google.api.services.drive.model.File file: files) {
                if (!file.getMimeType().equals("application/vnd.google-apps.folder") && file.getParents() != null && file.getParents().get(0).equals(folderId)){
                    automaticFiles.add(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return automaticFiles;
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
		String pageToken = savedStartPageToken;
		while (pageToken != null) {
			ChangeList changes = drive.changes().list(pageToken)
					.execute();
			for (Change change : changes.getChanges()) {
				if(change.getFile() == null){
					logger.log(Level.SEVERE, change.toPrettyString());
					continue;
				}
				boolean isFolder = change.getFile().getMimeType().contains("folder");
				boolean isDeleted = change.getRemoved();
				StringBuilder stringLog = new StringBuilder();
				if (isFolder) {
					stringLog.append("The folder ");
				} else {
					stringLog.append("The file ");
				}
				stringLog.append("with ID:[" + change.getFileId() + "], named: " + change.getFile().getName());
				if (isDeleted) {
					stringLog.append(" was removed");
				} else {
					stringLog.append(" was changed");
				}
				logger.log(Level.INFO, stringLog.toString());
			}
			if (changes.getNewStartPageToken() != null) {
				// Last page, save this token for the next polling interval
				savedStartPageToken = changes.getNewStartPageToken();
			}
			pageToken = changes.getNextPageToken();
		}
	}

	/**
	 * TODO: To try and test
	 * Uploads a file using either resumable or direct media upload.
	 */
	public com.google.api.services.drive.model.File uploadFile(boolean useDirectUpload, File file) throws IOException {
		com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
		fileMetadata.setName(file.getName());
		logger.log(Level.INFO, "Uploading a file " + file.getName() + " typed " + Files.probeContentType(Paths.get(file.getPath())));
		FileContent mediaContent = new FileContent(Files.probeContentType(Paths.get(file.getPath())), file);
		Drive.Files.Create insert = drive.files().create(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}

	/**
	 * TODO: To try and test
	 * Downloads a file using either resumable or direct media download.
	 */
	public void downloadFile(boolean useDirectDownload, String fileName, String fileid)
			throws IOException {
		// create parent directory (if necessary)

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        logger.log(Level.INFO, "Current folder is : " + s);
        Path destination = Paths.get(s + "/downloads/userID");
		File parentDir = new File(destination.toString());
		if (!parentDir.exists() && !parentDir.mkdirs()) {
			throw new IOException("Unable to create parent directory");
		}

		File output = new File(parentDir, fileName);
		OutputStream out = new FileOutputStream(output);
		//com.google.api.services.drive.model.File file = Login.googleDrive.drive.files().get(fileid).execute();
		Login.googleDrive.drive.files().get(fileid).executeMediaAndDownloadTo(out);
		//Login.googleDrive.drive.files().get(fileid).executeAndDownloadTo(out);
		//out.write(file.)
		/*MediaHttpDownloader downloader =
				new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
		downloader.setDirectDownloadEnabled(useDirectDownload);
		downloader.download(new GenericUrl(contentLinkDownload), out);*/
	}

    public OwnFile classifyFiles() throws IOException {
        String rootId = drive.files().get("root").setFields("id").execute().getId();
        com.google.api.services.drive.model.File rootFile = new com.google.api.services.drive.model.File();
        rootFile.setParents(new ArrayList<>());
        rootFile.setId(rootId);
        rootFile.setName("Drive Root");
        List<OwnFile> folders = new ArrayList<>();
        List<OwnFile> files = new ArrayList<>();

        OwnFile root = new OwnFile(rootFile, true);
        folders.add(root);
        List<com.google.api.services.drive.model.File> filesDrive = getFilesList();
        for (com.google.api.services.drive.model.File file : filesDrive) {
            if (file.getParents() != null) {
                if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                    folders.add(new OwnFile(file, true));
                } else {
                    files.add(new OwnFile(file, false));
                }
            }
        }
        for (OwnFile child : folders) {
            for (OwnFile possibleParent : folders) {
                if (child.file.getParents().size() > 0 && possibleParent.file.getId().equals(child.file.getParents().get(0))) {
                    possibleParent.addFolder(child);
                    break;
                }
            }
        }
        for (OwnFile child : files) {
            for (OwnFile possibleParent : folders) {
                if (possibleParent.file.getId().equals(child.file.getParents().get(0))) {
                    possibleParent.addFile(child);
                    break;
                }
            }
        }
        return root;
    }
}
