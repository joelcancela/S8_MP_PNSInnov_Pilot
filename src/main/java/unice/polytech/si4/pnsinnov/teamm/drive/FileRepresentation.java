package unice.polytech.si4.pnsinnov.teamm.drive;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.drive.exceptions.NullFileException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: To edit when abstraction will be done
 */
public class FileRepresentation implements Serializable {
    public File file;
    private List <FileRepresentation> folders;
    private List <FileRepresentation> files;

    public FileRepresentation(File file) {
        this.file = file;
        this.folders = new ArrayList <>();
        this.files = new ArrayList <>();
    }

    public boolean addFolder(FileRepresentation folder) throws NullFileException {
        if(file == null){
            throw new NullFileException("Trying to add null folder to file " + this.toString());
        }
        return folders.add(folder);
    }

    public boolean addFile(FileRepresentation file) throws NullFileException {
        if(file == null){
            throw new NullFileException("Trying to add null file to file " + this.toString());
        }
        return files.add(file);
    }

    public File getFile() throws NullFileException {
        if(file == null){
            throw new NullFileException("Google File is null for file " + this.toString());
        }
        return file;
    }

    public List <FileRepresentation> getFolders() {
        return folders;
    }

    public List <FileRepresentation> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return this.file.getName() == null ? "" : this.file.getName();
    }
}
