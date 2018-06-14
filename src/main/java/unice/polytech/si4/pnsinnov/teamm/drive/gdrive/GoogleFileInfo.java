package unice.polytech.si4.pnsinnov.teamm.drive.gdrive;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.FileClassifier;
import unice.polytech.si4.pnsinnov.teamm.drive.FileInfo;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;
import unice.polytech.si4.pnsinnov.teamm.drive.exceptions.NullFileException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GoogleFileInfo extends FileInfo<File> {

    public GoogleFileInfo(String filename){
        super();
        this.setName(filename);
    }

    public GoogleFileInfo(File file){
        super(file);
        id = file.getId();
        extension = file.getFileExtension();
        mimeType = file.getMimeType();
        name = file.getName();
        trashed = Optional.ofNullable(file.getTrashed());
        webViewLink = file.getWebViewLink();
    }

    private static final Logger logger = LogManager.getLogger(GoogleFileInfo.class);

    @Override
    public void moveFile(String folderName, boolean simulation, FileRepresentation<File> treeFile) {
        boolean folderExist = false;
        File fileMetaData = null;

        FileClassifier fileClassifier = new FileClassifier();

        if (folderName.equals("mimetype")) {
            folderName = fileClassifier.getFolderNameMimeType(this.getMimeType());
        } else if (folderName.equals("extension")) {
            folderName = fileClassifier.getFolderNameExtension(this.getExtension());
        }

        /* Check if the destination directory exists */
        try {
            if (simulation) {
                for (FileRepresentation<File> f : treeFile.getFolders()) {
                    if (f.file.getName().equals(folderName)) {
                        fileMetaData = f.file.getFile();
                        logger.log(Level.INFO, "Simulation : Folder " + folderName + " already exists");
                        folderExist = true;
                    }
                }
            } else {
                FileList result = gDriveSession.getDrive().files().list()
                        .setQ("mimeType='application/vnd.google-apps.folder' and trashed=false")
                        .execute();
                for (File f : result.getFiles()) {
                    if (f.getName().equals(folderName)) {
                        fileMetaData = f;
                        logger.log(Level.INFO, "Folder " + folderName + " already exists");
                        folderExist = true;
                    }
                }
            }

        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }

        /* Creates directory if it doesn't exist */
        if (!folderExist) {
            fileMetaData = new File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            if (simulation) {
                try {
                    treeFile.addChildFolder(new GoogleFileInfo(folderName));
                    logger.log(Level.INFO, "Simulation : Folder " + folderName + " created");
                } catch (NullFileException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    fileMetaData = gDriveSession.getDrive().files().create(fileMetaData).setFields("id").execute();
                    logger.log(Level.INFO, "Folder " + folderName + " created");
                } catch (IOException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        }

        /* Apply move */
        if (simulation) {
            FileRepresentation<File> parentRepresentation = null;
            FileRepresentation<File> sourceRepresentation = null;
            for (FileRepresentation<File> folderRepresentation : treeFile.getFolders()) {
                if (folderRepresentation.file.getName().equals(folderName)) {
                    parentRepresentation = folderRepresentation;
                } else if (folderRepresentation.file.getName().equals("_Automatic")) {
                    sourceRepresentation = folderRepresentation;
                }
            }
            try {
                parentRepresentation.addChildFile(this);
            } catch (NullFileException e) {
                e.printStackTrace();
            }
            sourceRepresentation.removeChildFile(file.getName());
        } else {

            // Retrieve the existing parents to remove

            File fileParents = null;
            try {
                fileParents = gDriveSession.getDrive().files().get(this.file.getId()).setFields("parents").execute();
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
                file = gDriveSession.getDrive().files().update(this.file.getId(), null)
                        .setAddParents(fileMetaData.getId())
                        .setRemoveParents(previousParents.toString())
                        .setFields("id, parents")
                        .execute();
            } catch (IOException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }
    }
}
