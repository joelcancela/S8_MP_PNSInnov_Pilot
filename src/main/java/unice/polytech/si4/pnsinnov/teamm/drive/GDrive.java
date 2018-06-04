package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.*;
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
	private HashMap<String, AbstractMap.SimpleEntry> exportedMimeMap;
	private List<String> gdriveMime;

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


		gdriveMime = new ArrayList<>();
		gdriveMime.add("application/vnd.google-apps.presentation");
		gdriveMime.add("application/vnd.google-apps.spreadsheet");
		gdriveMime.add("application/vnd.google-apps.document");


		// Mock mime exporting parameter
		exportedMimeMap = new HashMap();
		AbstractMap.SimpleEntry<String, String> presentation = new AbstractMap.SimpleEntry<>("application/vnd.oasis.opendocument.presentation", ".odp");
		exportedMimeMap.put("application/vnd.google-apps.presentation", presentation);
		AbstractMap.SimpleEntry<String, String> spreadsheet = new AbstractMap.SimpleEntry<>("application/vnd.oasis.opendocument.spreadsheet", ".ods");
		exportedMimeMap.put("application/vnd.google-apps.spreadsheet", spreadsheet);
		AbstractMap.SimpleEntry<String, String> document = new AbstractMap.SimpleEntry<>("application/vnd.oasis.opendocument.text", ".odt");
		exportedMimeMap.put("application/vnd.google-apps.document", document);
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
		try {
			List<OwnFile> found = Login.googleDrive.classifyFiles().getFolders().stream().filter(f -> f.file.getName().equals("_Automatic")).collect(Collectors.toList());
			if (!found.isEmpty()) {
				addAllFiles(found.get(0), automaticFiles);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return automaticFiles;
	}

	public void addAllFiles(OwnFile root, List<com.google.api.services.drive.model.File> toAdd) {
		toAdd.addAll(root.getFiles().stream().map(f -> f.file).collect(Collectors.toList()));
		for (OwnFile ownfile : root.getFolders()) {
			addAllFiles(ownfile, toAdd);
		}
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
			logger.log(Level.SEVERE, "Auto-tidying won't be available as you are running the server on localhost");
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
				if (change.getFile() == null) {
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

	public String getFileName(String fileid) throws IOException {
		com.google.api.services.drive.model.File file = Login.googleDrive.drive.files().get(fileid).execute();
		if(exportedMimeMap.containsKey(file.getMimeType())){
			return file.getName()+exportedMimeMap.get(file.getMimeType()).getValue();
		}else{
			return file.getName();
		}
	}


	public InputStream downloadFileDirect(String fileid) throws IOException {
		com.google.api.services.drive.model.File file = Login.googleDrive.drive.files().get(fileid).execute();
		String mimetype = file.getMimeType();
		if (gdriveMime.contains(mimetype)) {
			return Login.googleDrive.drive.files().export(fileid, exportedMimeMap.get(mimetype).getKey().toString())
					.executeMediaAsInputStream();
		} else {
			return Login.googleDrive.drive.files().get(fileid).executeMediaAsInputStream();
		}
	}

	/**
	 * Downloads a file using either resumable or direct media download.
	 */
	public String downloadFile(boolean useDirectDownload, String fileid, String exportedMime) throws IOException {
		com.google.api.services.drive.model.File file = Login.googleDrive.drive.files().get(fileid).execute();
		String fileName = file.getName();
		String mimetype = file.getMimeType();


		//GDrive file get exported mime and add extension
		if (gdriveMime.contains(mimetype)) {
			exportedMime = exportedMimeMap.get(mimetype).getKey().toString();
			fileName += exportedMimeMap.get(mimetype).getValue();
		}

		Path currentRelativePath = Paths.get("");
		Path destination = Paths.get(currentRelativePath.toAbsolutePath().toString() + "/downloads");

		File parentDir = new File(destination.toString());
		if (!parentDir.exists() && !parentDir.mkdirs()) {
			throw new IOException("Unable to create parent directory");
		}

		File output = new File(parentDir, fileName);
		OutputStream out = new FileOutputStream(output);


		if (gdriveMime.contains(mimetype)) {
			Login.googleDrive.drive.files().export(fileid, exportedMime).executeMediaAndDownloadTo(out);
		} else {
			Login.googleDrive.drive.files().get(fileid).executeMediaAndDownloadTo(out);
		}
		return output.getPath();
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
