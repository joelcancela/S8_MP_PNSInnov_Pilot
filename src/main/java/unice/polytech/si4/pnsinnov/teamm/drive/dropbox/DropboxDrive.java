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

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getFileName(DropboxSession session, String id) throws DbxException {
        return session.getDropboxClient().files().getMetadata("id:" + id).getName();
    }

    public InputStream downloadFileDirect(DropboxSession session, String id) throws DbxException {
        return session.getDropboxClient().files().download("id:" + id).getInputStream();
    }

    public File downloadFile(DropboxSession session, String id) throws DbxException, IOException {
        InputStream is = downloadFileDirect(session, id);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        File targetFile = new File(getFileName(session, id));
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        return targetFile;
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
            List <Metadata> entries = session.getDropboxClient().files()
                    .listFolderBuilder(path)
                    .withIncludeDeleted(false)
                    .start()
                    .getEntries();
            List <Metadata> trashed = session.getDropboxClient()
                    .files()
                    .listFolderBuilder(path)
                    .withIncludeDeleted(true)
                    .start()
                    .getEntries()
                    .stream()
                    .filter(e -> ! entries.contains(e))
                    .collect(Collectors.toList());
            for (Metadata entry : entries) {
                DropboxFileInfo entryInfo = new DropboxFileInfo(entry);
                if (trashed.contains(entry)) {
                    entryInfo.setTrashed(true);
                }
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

    public void uploadFile(DropboxSession session, File file) throws DbxException, IOException {
        session.getDropboxClient().files().upload("/" + file.getName()).uploadAndFinish(new FileInputStream(file));
    }
}
