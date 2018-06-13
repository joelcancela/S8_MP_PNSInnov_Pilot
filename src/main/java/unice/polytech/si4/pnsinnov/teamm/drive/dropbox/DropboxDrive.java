package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.filerequests.FileRequest;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;
import unice.polytech.si4.pnsinnov.teamm.drive.exceptions.NullFileException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Class DropboxDrive
 *
 * @author Joël CANCELA VAZ
 */
public class DropboxDrive {

    private static Logger logger = LogManager.getLogger(DropboxDrive.class);
    private DbxAppInfo appInfo = null;
    private static DropboxDrive INSTANCE;

    public DropboxDrive() {

        try {
            URL secretsPath = getClass().getResource("/dropbox_secrets.json");
            logger.log(Level.INFO, secretsPath);
            File dropboxSecret = new File(secretsPath.toURI());
            appInfo = DbxAppInfo.Reader.readFromFile(dropboxSecret);
            logger.log(Level.DEBUG, appInfo.toString());
        } catch (com.dropbox.core.json.JsonReader.FileLoadException | URISyntaxException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        logger.log(Level.DEBUG, appInfo.toString());
        logger.log(Level.DEBUG, appInfo.getHost());
        logger.log(Level.DEBUG, appInfo.getKey());
        logger.log(Level.DEBUG, appInfo.getSecret());
        logger.log(Level.DEBUG, appInfo.toString());
        // Run through Dropbox API authorization process
    }

    public static DropboxDrive getDropboxDrive() {
        if (INSTANCE == null) {
            INSTANCE = new DropboxDrive();
        }
        return INSTANCE;
    }

    public DropboxSession createNewSession(String userid) {
        DbxRequestConfig requestConfig = new DbxRequestConfig(userid);//FIXME: to change
        DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo);
        return new DropboxSession(requestConfig, webAuth, appInfo, userid);
    }

    public FileRepresentation <Metadata> buildFileTree(DropboxSession session) {
        try {
            DropboxFileInfo rootInfo = new DropboxFileInfo("Drive Root");
            /* Pas d'ID pour la racine, API de gitan */
            rootInfo.setId("rootId");
            FileRepresentation <Metadata> root = new FileRepresentation <>(rootInfo);
            return buildTree(session, root);
        } catch (DbxException | NullFileException e) {
            logger.log(Level.ERROR, e.getMessage());
            return null; //TODO
        }
    }

    private FileRepresentation <Metadata> buildTree(DropboxSession session, FileRepresentation <Metadata> fileData) throws DbxException, NullFileException {
        Metadata data = fileData.getFile().getFile();
        String path = data == null ? "" : data.getPathLower();
        if (((DropboxFileInfo) fileData.getFile()).isFolder()) {
            List <Metadata> entries = session.getDropboxClient().files().listFolder(path).getEntries();
//        List <Metadata> entriesIncludingTrashed = session.getDropboxClient().files().listFolderBuilder("").withIncludeDeleted(true).start().getEntries();
            for (Metadata entry : entries) {
                DropboxFileInfo entryInfo = new DropboxFileInfo(entry);
                FileRepresentation <Metadata> entryR = new FileRepresentation <>(entryInfo);
                if (entryInfo.isFolder()) {
                    fileData.addFolder(buildTree(session, entryR));
                } else {
                    fileData.addFile(buildTree(session, entryR));
                }
            }
        }
        return fileData;
    }

}
