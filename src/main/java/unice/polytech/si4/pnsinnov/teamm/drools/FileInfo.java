package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import java.io.IOException;
import java.util.List;

public class FileInfo {
    private File file;
    private String extension;
    private String mimeType;
    private String nameFile;
    private static final Logger logger = LogManager.getLogger(FileInfo.class);

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

    public void moveFile(String folderName) {
        boolean folderExist = false;
        File fileMetaData = null;
        try {
            FileList result = Login.googleDrive.drive.files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder'")
                    .execute();
            for (File f : result.getFiles()) {
                if (f.getName().equals(folderName)) {
                    fileMetaData = f;
                    logger.log(Level.INFO, "Folder " + folderName + " already exists");
                    folderExist = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!folderExist) {
            fileMetaData = new File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            try {
                fileMetaData = Login.googleDrive.drive.files().create(fileMetaData).setFields("id").execute();
                logger.log(Level.INFO, "Folder " + folderName + " created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Retrieve the existing parents to remove

        File fileParents = null;
        try {
            fileParents = Login.googleDrive.drive.files().get(this.file.getId()).setFields("parents").execute();
        } catch (IOException e) {
            e.printStackTrace();
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
            file = Login.googleDrive.drive.files().update(this.file.getId(), null)
                    .setAddParents(fileMetaData.getId())
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


