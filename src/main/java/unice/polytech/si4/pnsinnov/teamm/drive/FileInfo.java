package unice.polytech.si4.pnsinnov.teamm.drive;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;

import java.util.Optional;

/**
 * FIXME: FileInfo vs FileRepresentation vs FileClassifier
 */
public abstract class FileInfo<T> {
    protected T file;
    protected String extension;
    protected String mimeType;
    protected String name;
    protected String id;
    private static final Logger logger = LogManager.getLogger(FileInfo.class);
    private boolean acceptedMimeType;
    private boolean acceptedExtensions;
    protected GDriveSession session;
    protected Optional<Boolean> trashed;
    protected String webViewLink;

    public FileInfo(T file) {
        this();
        this.file = file;
    }

    public FileInfo() {
        this.extension = "";
        this.mimeType = "";
        this.name = "";
        this.id = "";
        this.webViewLink = "";
        this.acceptedExtensions = false;
        this.acceptedMimeType = false;
        this.trashed = Optional.empty();
    }

    public abstract void moveFile(String folderName, boolean simulation, FileRepresentation<T> treeFile);

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setFile(T file) {
        this.file = file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.replace(" ", "").replace("id:", "");
    }

    public void setSession(GDriveSession session) {
        this.session = session;
    }

    public T getFile() {
        return file;
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

    public boolean getTrashed() {
        return trashed.orElse(false);
    }

    public void setTrashed(boolean trashed) {
        this.trashed = Optional.of(trashed);
    }

    public String getWebViewLink() {
        return webViewLink;
    }

    public void setWebViewLink(String webViewLink) {
        this.webViewLink = webViewLink;
    }
}


