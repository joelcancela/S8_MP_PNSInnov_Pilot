package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;
import unice.polytech.si4.pnsinnov.teamm.drools.exceptions.UnhandledExtension;
import unice.polytech.si4.pnsinnov.teamm.drools.exceptions.UnhandledMimeType;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
			throw new UnhandledMimeType();
		}
	}

	public String getFolderNameExtension(String extension) {
		if (isAcceptedExtension(extension)) {
			return this.acceptedExtensions.get(extension);
		} else {
			throw new UnhandledExtension();
		}
	}

	public Set<String> getMimeTypes(){
		return this.acceptedMimeTypes.keySet();
	}
}
