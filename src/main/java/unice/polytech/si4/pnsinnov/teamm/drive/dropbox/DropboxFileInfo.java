package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.*;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.FileClassifier;
import unice.polytech.si4.pnsinnov.teamm.drive.FileInfo;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;
import unice.polytech.si4.pnsinnov.teamm.drive.exceptions.NullFileException;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GoogleFileInfo;

import java.io.IOException;
import java.util.List;

public class DropboxFileInfo extends FileInfo <Metadata> {

    private static final Logger logger = LogManager.getLogger(DropboxFileInfo.class);


    public DropboxFileInfo(String filename) {
        super();
        this.setName(filename);
    }

    public DropboxFileInfo(Metadata file) {
        super(file);
        this.setId(isFolder() ? ((FolderMetadata) file).getId() : ((FileMetadata) file).getId());
        this.setName(file.getName());
//        webViewLink = isFolder() ? ((FolderMetadata) file).
    }

    public boolean isFolder() {
        /* The root doesn't seem to be a folder... */
        return name.equals("Drive Root") || file instanceof FolderMetadata;
    }

    @Override
    public void moveFile(String folderName, boolean simulation, FileRepresentation <Metadata> treeFile) {

        boolean folderExist = false;
        Metadata fileMetaData = null;

        FileClassifier fileClassifier = new FileClassifier();

        if (folderName.equals("mimetype")) {
            folderName = fileClassifier.getFolderNameMimeType(this.getMimeType());
        } else if (folderName.equals("extension")) {
            folderName = fileClassifier.getFolderNameExtension(this.getExtension());
        }

        /* Check if the destination directory exists */
        try {
            if (simulation) {
                for (FileRepresentation <Metadata> f : treeFile.getFolders()) {
                    if (f.file.getName().equals(folderName)) {
                        fileMetaData = f.file.getFile();
                        logger.log(Level.INFO, "Simulation : Folder " + folderName + " already exists");
                        folderExist = true;
                    }
                }
            } else {
                ListFolderResult result = dropboxSession.getDropboxClient().files()
                        .listFolderBuilder("")
                        .withIncludeDeleted(false)
                        .withRecursive(true)
                        .withIncludeMediaInfo(true)
                        .start();
                for (Metadata f : result.getEntries()) {
                    if (f.getName().equals(folderName)) {
                        fileMetaData = f;
                        logger.log(Level.INFO, "Folder " + folderName + " already exists");
                        folderExist = true;
                    }
                }
            }
        } catch (DbxException e) {
            logger.log(Level.ERROR, e.getMessage());
        }

        /* Creates directory if it doesn't exist */
        if (! folderExist) {
            fileMetaData = new Metadata(folderName);
            if (simulation) {
                try {
                    treeFile.addChildFolder(new DropboxFileInfo(folderName));
                    logger.log(Level.INFO, "Simulation : Folder " + folderName + " created");
                } catch (NullFileException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            } else {
                try {
                    dropboxSession.getDropboxClient().files().createFolderV2(fileMetaData.getPathLower()).getMetadata();
                    logger.log(Level.INFO, "Folder " + folderName + " created");
                } catch (DbxException e) {
                    logger.log(Level.ERROR, e.getMessage());
                }
            }
        }

        if (simulation) {
            FileRepresentation <Metadata> parentRepresentation = null;
            FileRepresentation <Metadata> sourceRepresentation = null;
            logger.log(Level.ERROR, treeFile);
            logger.log(Level.ERROR, treeFile.getFolders());
            for (FileRepresentation <Metadata> folderRepresentation : treeFile.getFolders()) {
                if (folderRepresentation.file.getName().equals(folderName)) {
                    parentRepresentation = folderRepresentation;
                } else if (folderRepresentation.file.getName().equals("_Automatic")) {
                    sourceRepresentation = folderRepresentation;
                }
            }
            try {
                parentRepresentation.addChildFile(this);
            } catch (NullFileException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
            sourceRepresentation.removeChildFile(file.getName());
        } else {

            // Move the file to the new folder
            try {
                logger.log(Level.INFO, "Move file " + this.file.getName() + " to " + folderName);
                file = dropboxSession.getDropboxClient().files().moveV2(this.file.getName(), folderName).getMetadata();
            } catch (DbxException e) {
                logger.log(Level.ERROR, e.getMessage());
            }
        }

    }

}
