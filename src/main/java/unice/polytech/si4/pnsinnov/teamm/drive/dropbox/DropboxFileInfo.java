package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import unice.polytech.si4.pnsinnov.teamm.drive.FileInfo;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;

public class DropboxFileInfo extends FileInfo <Metadata> {

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

    public boolean isFolder(){
        /* The root doesn't seem to be a folder... */
        return name.equals("Drive Root") || file instanceof FolderMetadata;
    }

    @Override
    public void moveFile(String folderName, boolean simulation, FileRepresentation <Metadata> treeFile) {

    }

}
