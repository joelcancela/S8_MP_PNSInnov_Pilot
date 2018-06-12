package unice.polytech.si4.pnsinnov.teamm.drive.dropbox;

import com.dropbox.core.v1.DbxEntry;
import unice.polytech.si4.pnsinnov.teamm.drive.FileInfo;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;

public class DropboxTempFile extends FileInfo <DbxEntry.File> {
    @Override
    public void setName(String name) {
        this.name = this.file.name;
    }

    @Override
    public void setId(String replace) {

    }

    @Override
    public void moveFile(String folderName, boolean simulation, FileRepresentation <DbxEntry.File> treeFile) {

    }

    @Override
    public void setMimeType(String s) {
    }

    @Override
    public String getName() {
        return null;
    }
}
