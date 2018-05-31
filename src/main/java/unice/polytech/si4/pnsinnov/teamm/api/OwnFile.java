package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OwnFile implements Serializable {
    public File file;
    private List<OwnFile> folders;
    private List<OwnFile> files;
    private boolean folder;

    public OwnFile(File file, boolean isFolder) {
        this.file = file;
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
        this.folder = isFolder;
    }

    public boolean addFolder(OwnFile folder){
        return folders.add(folder);
    }

    public boolean addFile(OwnFile file){
        return files.add(file);
    }

    public boolean isFolder() {
        return folder;
    }

    public File getFile() {
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
        return this.file.getName();
    }
}
