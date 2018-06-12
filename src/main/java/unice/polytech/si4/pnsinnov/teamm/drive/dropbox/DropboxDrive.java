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
            String secretsPath = getClass().getResource("/dropbox_secrets.json").toString().replace("file:/", "");
            File dropboxSecret = new File(secretsPath);
            logger.log(Level.ERROR, secretsPath);
            logger.log(Level.ERROR, secretsPath);
            logger.log(Level.ERROR, secretsPath);
            logger.log(Level.ERROR, secretsPath);
            logger.log(Level.ERROR, secretsPath);
            appInfo = DbxAppInfo.Reader.readFromFile(dropboxSecret);
            logger.log(Level.DEBUG, appInfo.toString());
        } catch (com.dropbox.core.json.JsonReader.FileLoadException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        logger.log(Level.DEBUG, appInfo.toString());
        logger.log(Level.DEBUG, appInfo.toString());
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
            List <Metadata> entries = session.getDropboxClient().files().listFolder("").getEntries();
            List <Metadata> entriesIncludingTrashed = session.getDropboxClient().files().listFolderBuilder("").withIncludeDeleted(true).start().getEntries();
//			FileRequest entries2 = session.getDropboxClient().fileRequests().list().getFileRequests().get(0);
//			FileRequest entries2 = session.getDropboxClient().fileRequests();
            DropboxFileInfo rootInfo = new DropboxFileInfo("Drive Root");
            /* Pas d'ID pour la racine, API de gitan */
            rootInfo.setId("rootId");
            FileRepresentation <Metadata> root = new FileRepresentation <>(rootInfo);
            for (Metadata entry : entries) {
//				((FileMetadata) entry)
                DropboxFileInfo entryInfo = new DropboxFileInfo(entry);
                FileRepresentation <Metadata> entryR = new FileRepresentation <>(entryInfo);
                if (entryInfo.isFolder()) {
                    root.addFolder(entryR);
                } else {
                    root.addFile(entryR);
                }
            }
            return root;
        } catch (DbxException | NullFileException e) {
            logger.log(Level.ERROR, e.getMessage());
            return null; //TODO
        }
    }
}
