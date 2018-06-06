package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;
import unice.polytech.si4.pnsinnov.teamm.exceptions.NullFileException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OwnFile implements Serializable {
    public File file;
    private List <OwnFile> folders;
    private List <OwnFile> files;

    public OwnFile(File file) {
        this.file = file;
        this.folders = new ArrayList <>();
        this.files = new ArrayList <>();
    }

    public boolean addFolder(OwnFile folder) throws NullFileException {
        if(file == null){
            throw new NullFileException("Trying to add null folder to file " + this.toString());
        }
        return folders.add(folder);
    }

    public boolean addFile(OwnFile file) throws NullFileException {
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

    public List <OwnFile> getFolders() {
        return folders;
    }

    public List <OwnFile> getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return this.file.getName() == null ? "" : this.file.getName();
    }
}
