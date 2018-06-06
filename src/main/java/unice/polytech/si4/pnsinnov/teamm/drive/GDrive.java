package unice.polytech.si4.pnsinnov.teamm.drive;

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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;
import unice.polytech.si4.pnsinnov.teamm.exceptions.NullFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class GDrive
 *
 * @author Joël CANCELA VAZ
 */
public class GDrive {
	private final static Logger logger = LogManager.getLogger(GDrive.class.getName());
	private final File DATA_STORE_DIR = new File("target/store");
	private FileDataStoreFactory dataStoreFactory;
	private HashMap<String, AbstractMap.SimpleEntry> exportedMimeMap;
	private List<String> gdriveMime;
	private static GDrive gDrive;
	private GoogleAuthorizationCodeFlow flow;

	public static GDrive getGDrive() {
		if (GDrive.gDrive == null) {
			try {
				GDrive.gDrive = new GDrive();
			} catch (IOException | GeneralSecurityException e) {
				logger.log(Level.ERROR, e.getMessage());
			}
		}
		return gDrive;
	}

	public GoogleAuthorizationCodeFlow getFlow() {
		return flow;
	}

	private GDrive() throws IOException, GeneralSecurityException {
		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
		dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(GDrive.class.getResourceAsStream("/client_secrets.json")));
		// set up authorization code flow
		flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, JSON_FACTORY, clientSecrets,
				Collections.singleton(DriveScopes.DRIVE)).setDataStoreFactory(dataStoreFactory)
				.build();


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

	public List<com.google.api.services.drive.model.File> getFilesList(GDriveSession session) throws IOException {
		Drive.Files.List request = session.getDrive().files().list().setFields("nextPageToken, files");
		List<com.google.api.services.drive.model.File> files = new ArrayList<>(request.execute().getFiles());
		if (files.isEmpty()) {
			logger.log(Level.INFO, "No files found.");
		} else {
			logger.log(Level.INFO, files.size() + " Files found.");
		}
		return files;
	}

	public List<com.google.api.services.drive.model.File> getAutomaticFilesList(GDriveSession session) {
		List<com.google.api.services.drive.model.File> automaticFiles = new ArrayList<>();
		try {
			List<OwnFile> found = this.classifyFiles(session).getFolders().stream().filter(f -> f.file.getName().equals("_Automatic")).collect(Collectors.toList());
			if (!found.isEmpty()) {
				addAllFiles(found.get(0), automaticFiles);
			}
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
		return automaticFiles;
	}

	public void addAllFiles(OwnFile root, List<com.google.api.services.drive.model.File> toAdd) {
		toAdd.addAll(root.getFiles().stream().map(f -> f.file).collect(Collectors.toList()));
		for (OwnFile ownfile : root.getFolders()) {
			addAllFiles(ownfile, toAdd);
		}
	}

	public Channel subscribeToChanges(GDriveSession session) {//TODO: watch mutliples sessions
		Channel notifications = watchChange(session.getDrive(), session.userID + "-watches", ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/notifications&userId=" + session.userID);
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
			logger.log(Level.ERROR, "Auto-tidying won't be available as you are running the server on localhost");
		}
		return null;
	}

	public void unsubscribeChannel(GDriveSession session, Channel channel) throws IOException {
		session.getDrive().channels().stop(channel);
	}

	public void getChanges(GDriveSession session) throws IOException {
		String pageToken = session.savedStartPageToken;
		while (pageToken != null) {
			ChangeList changes = session.getDrive().changes().list(pageToken)
					.execute();
			for (Change change : changes.getChanges()) {
				if (change.getFile() == null) {
					logger.log(Level.ERROR, change.toPrettyString());
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
				session.savedStartPageToken = changes.getNewStartPageToken();
			}
			pageToken = changes.getNextPageToken();
		}
	}

	/**
	 * TODO: To try and test
	 * Uploads a file using either resumable or direct media upload.
	 */
	public com.google.api.services.drive.model.File uploadFile(GDriveSession session, boolean useDirectUpload, File file) throws IOException {
		com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
		fileMetadata.setName(file.getName());
		logger.log(Level.INFO, "Uploading a file " + file.getName() + " typed " + Files.probeContentType(Paths.get(file.getPath())));
		FileContent mediaContent = new FileContent(Files.probeContentType(Paths.get(file.getPath())), file);
		Drive.Files.Create insert = session.getDrive().files().create(fileMetadata, mediaContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		return insert.execute();
	}

	public String getFileName(GDriveSession session, String fileid) throws IOException {
		com.google.api.services.drive.model.File file = session.getDrive().files().get(fileid).execute();
		if(exportedMimeMap.containsKey(file.getMimeType())){
			return file.getName()+exportedMimeMap.get(file.getMimeType()).getValue();
		}else{
			return file.getName();
		}
	}


	public InputStream downloadFileDirect(GDriveSession session, String fileid) throws IOException {
		com.google.api.services.drive.model.File file = session.getDrive().files().get(fileid).execute();
		String mimetype = file.getMimeType();
		if (gdriveMime.contains(mimetype)) {
			return session.getDrive().files().export(fileid, exportedMimeMap.get(mimetype).getKey().toString())
					.executeMediaAsInputStream();
		} else {
			return session.getDrive().files().get(fileid).executeMediaAsInputStream();
		}
	}

	public Void deleteFile(GDriveSession session, String fileid) throws IOException {
		return session.getDrive().files().delete(fileid).execute();
	}

	/**
	 * Downloads a file using either resumable or direct media download.
	 */
	public String downloadFile(GDriveSession session, boolean useDirectDownload, String fileid, String exportedMime) throws IOException {
		com.google.api.services.drive.model.File file = session.getDrive().files().get(fileid).execute();
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
			session.getDrive().files().export(fileid, exportedMime).executeMediaAndDownloadTo(out);
		} else {
			session.getDrive().files().get(fileid).executeMediaAndDownloadTo(out);
		}
		return output.getPath();
	}


	public OwnFile classifyFiles(GDriveSession session) throws IOException {
		String rootId = session.getDrive().files().get("root").setFields("id").execute().getId();
		com.google.api.services.drive.model.File rootFile = new com.google.api.services.drive.model.File();
		rootFile.setParents(new ArrayList<>());
		rootFile.setId(rootId);
		rootFile.setName("Drive Root");
		List<OwnFile> folders = new ArrayList<>();
		List<OwnFile> files = new ArrayList<>();

		OwnFile root = new OwnFile(rootFile);
		folders.add(root);
		List<com.google.api.services.drive.model.File> filesDrive = getFilesList(session);
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
				if (child.file.getParents().size() > 0 && possibleParent.file.getId().equals(child.file.getParents().get(0))) {
					try {
						possibleParent.addFolder(child);
					} catch (NullFileException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		for (OwnFile child : files) {
			for (OwnFile possibleParent : folders) {
				if (possibleParent.file.getId().equals(child.file.getParents().get(0))) {
					try {
						possibleParent.addFile(child);
					} catch (NullFileException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return root;
	}


}
