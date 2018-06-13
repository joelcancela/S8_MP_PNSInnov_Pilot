package unice.polytech.si4.pnsinnov.teamm.drive;

import unice.polytech.si4.pnsinnov.teamm.drive.exceptions.NullFileException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: To edit when abstraction will be done
 */
public class FileRepresentation <T> implements Serializable {
    public FileInfo <T> file;
    private List <FileRepresentation <T>> folders;
    private List <FileRepresentation <T>> files;

    public FileRepresentation(FileInfo<T> file) {
        this.file = file;
        this.folders = new ArrayList <>();
        this.files = new ArrayList <>();
    }

    public boolean addFolder(FileRepresentation<T> folder) throws NullFileException {
        if (file == null) {
            throw new NullFileException("Trying to add null folder to file " + this.toString());
        }
        return folders.add(folder);
    }

    public boolean addFile(FileRepresentation<T> file) throws NullFileException {
        if (file == null) {
            throw new NullFileException("Trying to add null file to file " + this.toString());
        }
        return files.add(file);
    }

    public FileInfo<T> getFile() throws NullFileException {
        if (file == null) {
            throw new NullFileException("Google File is null for file " + this.toString());
        }
        return file;
    }

    public List <FileRepresentation <T>> getFolders() {
        return folders;
    }

    public List <FileRepresentation <T>> getFiles() {
        return files;
    }

    public void addChildFolder(FileInfo <T> folder) throws NullFileException {
        folder.setName(folder.getName());
        folder.setId(folder.getName());
        folder.setMimeType("application/vnd.google-apps.folder");
        addFolder(new FileRepresentation <>(folder));
    }

    public void addChildFile(FileInfo <T> file) throws NullFileException {
        addFile(new FileRepresentation <>(file));
    }

    public void removeChildFile(String name) {
        FileRepresentation<T> childfile = null;
        for (FileRepresentation<T> fileRepresentation : this.files) {
            if (fileRepresentation.file.getName().equals(name)) {
                childfile = fileRepresentation;
            }
        }
        if (childfile != null) {
            this.files.remove(childfile);
        }
    }

    @Override
    public String toString() {
        return file.getName() == null ? "" : file.getName();
    }
}
