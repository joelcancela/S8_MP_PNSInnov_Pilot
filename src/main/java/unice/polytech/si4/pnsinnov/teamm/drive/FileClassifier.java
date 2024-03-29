package unice.polytech.si4.pnsinnov.teamm.drive;

import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledExtensionRException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnhandledMimeTypeRException;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by Nassim B on 6/4/18.
 */
public class FileClassifier {

	private HashMap<String, String> acceptedMimeTypes;
	private HashMap<String, String> acceptedExtensions;

	public FileClassifier() {
		this.acceptedMimeTypes = new HashMap<>();
		this.acceptedMimeTypes.put("application/vnd.google-apps.spreadsheet", "GSpreadsheet");
		this.acceptedMimeTypes.put("application/vnd.google-apps.document", "GDocs");

		this.acceptedExtensions = new HashMap<>();
		this.acceptedExtensions.put("pdf","PDF");
		this.acceptedExtensions.put("svg","SVG");
		this.acceptedExtensions.put("png","PNG");
	}

	public boolean isAcceptedMimeType(String mimeType) {
		return this.acceptedMimeTypes.keySet().contains(mimeType);
	}

	public boolean isAcceptedExtension(String extension){
		return this.acceptedExtensions.keySet().contains(extension);
	}

	public String getFolderNameMimeType(String mimeType) {
		if (isAcceptedMimeType(mimeType)) {
			return this.acceptedMimeTypes.get(mimeType);
		} else {
			throw new UnhandledMimeTypeRException();
		}
	}

	public String getFolderNameExtension(String extension) {
		if (isAcceptedExtension(extension)) {
			return this.acceptedExtensions.get(extension);
		} else {
			throw new UnhandledExtensionRException();
		}
	}

	public Set<String> getMimeTypes(){
		return this.acceptedMimeTypes.keySet();
	}
}
