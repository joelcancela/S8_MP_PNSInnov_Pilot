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
        id = isFolder() ? ((FolderMetadata) file).getId() : ((FileMetadata) file).getId();
        name = file.getName();
    }

    public boolean isFolder(){
        return file instanceof FolderMetadata;
    }

    @Override
    public void moveFile(String folderName, boolean simulation, FileRepresentation <Metadata> treeFile) {

    }

}
