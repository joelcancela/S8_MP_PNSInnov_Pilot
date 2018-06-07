package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import java.io.IOException;
import java.util.List;

public class FileInfo {
    private File file;
    private String extension;
    private String mimeType;
    private String nameFile;
    private static final Logger logger = LogManager.getLogger(FileInfo.class);
    private boolean acceptedMimeType;
    private boolean acceptedExtensions;
	private GDriveSession session;

    public FileInfo() {
    }


    public String getExtension() {
        return extension;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

	public void setSession(GDriveSession session) {
		this.session = session;
	}

    public void moveFile(String folderName) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
        boolean folderExist = false;
        File fileMetaData = null;

        FileClassifier fileClassifier = new FileClassifier();

        if (folderName.equals("mimetype")) {
            folderName = fileClassifier.getFolderNameMimeType(this.getMimeType());
        } else if (folderName.equals("extension")) {
            folderName = fileClassifier.getFolderNameExtension(this.getExtension());
        }

        try {
            FileList result = session.getDrive().files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder' and trashed=false")
                    .execute();
            for (File f : result.getFiles()) {
                System.out.println(f.getName());
                if (f.getName().equals(folderName)) {
                    fileMetaData = f;
                    logger.log(Level.INFO, "Folder " + folderName + " already exists");
                    folderExist = true;
                }
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }


        if (!folderExist) {
            fileMetaData = new File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            try {
                fileMetaData = session.getDrive().files().create(fileMetaData).setFields("id").execute();
                logger.log(Level.INFO, "Folder " + folderName + " created");
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }


        // Retrieve the existing parents to remove

        File fileParents = null;
        try {
            fileParents = session.getDrive().files().get(this.file.getId()).setFields("parents").execute();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }

        StringBuilder previousParents = new StringBuilder();
        if (fileParents != null) {
            List<String> parents = fileParents.getParents();
            if (parents != null) {
                for (String parent : parents) {
                    previousParents.append(parent);
                    previousParents.append(',');
                }
            }
        }
        // Move the file to the new folder
        try {
            logger.log(Level.INFO, "Move file " + this.file.getName() + " to " + folderName);
            file = session.getDrive().files().update(this.file.getId(), null)
                    .setAddParents(fileMetaData.getId())
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
    }

    public boolean isAcceptedMimeType() {
        return acceptedMimeType;
    }

    public void setAcceptedMimeType(boolean acceptedMimeType) {
        this.acceptedMimeType = acceptedMimeType;
    }

    public boolean isAcceptedExtensions() {
        return acceptedExtensions;
    }

    public void setAcceptedExtensions(boolean acceptedExtensions) {
        this.acceptedExtensions = acceptedExtensions;
    }
}


