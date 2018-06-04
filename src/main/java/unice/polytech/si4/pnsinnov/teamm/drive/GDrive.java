package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Channel;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;
import unice.polytech.si4.pnsinnov.teamm.config.ConfigurationLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	private HashMap<String, AbstractMap.SimpleEntry> exportedMimeMap;
	private List<String> gdriveMime;

	private Drive drive;


	public GDrive(Drive drive) {
		this.drive = drive;
		// set up authorization code flow
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
			List<OwnFile> found = this.classifyFiles().getFolders().stream().filter(f -> f.file.getName().equals("_Automatic")).collect(Collectors.toList());
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

	public Drive getDrive() {
		return drive;
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
		com.google.api.services.drive.model.File file = this.drive.files().get(fileid).execute();
		if(exportedMimeMap.containsKey(file.getMimeType())){
			return file.getName()+exportedMimeMap.get(file.getMimeType()).getValue();
		}else{
			return file.getName();
		}
	}


	public InputStream downloadFileDirect(String fileid) throws IOException {
		com.google.api.services.drive.model.File file = this.drive.files().get(fileid).execute();
		String mimetype = file.getMimeType();
		if (gdriveMime.contains(mimetype)) {
			return this.drive.files().export(fileid, exportedMimeMap.get(mimetype).getKey().toString())
					.executeMediaAsInputStream();
		} else {
			return this.drive.files().get(fileid).executeMediaAsInputStream();
		}
	}

	/**
	 * Downloads a file using either resumable or direct media download.
	 */
	public String downloadFile(boolean useDirectDownload, String fileid, String exportedMime) throws IOException {
		com.google.api.services.drive.model.File file = this.drive.files().get(fileid).execute();
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
			this.drive.files().export(fileid, exportedMime).executeMediaAndDownloadTo(out);
		} else {
			this.drive.files().get(fileid).executeMediaAndDownloadTo(out);
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

	public Channel subscribeToChanges(String userID) {//TODO: watch mutliples sessions
		Channel notifications = watchChange(drive, userID + "-watches", ConfigurationLoader.getInstance().getHost() + "/PrivateMemo/api/notifications");
		logger.log(Level.INFO, "Watching for changes on Google Drive with channel : " + userID + "-watches");
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


}
