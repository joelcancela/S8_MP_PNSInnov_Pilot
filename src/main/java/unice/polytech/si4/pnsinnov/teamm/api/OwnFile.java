package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;

import java.util.ArrayList;
import java.util.List;

public class OwnFile {
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

    @Override
    public String toString() {
        return "OwnFile{" +
                "file=" + file +
                ", folders=" + folders +
                ", files=" + files +
                '}';
    }
}
