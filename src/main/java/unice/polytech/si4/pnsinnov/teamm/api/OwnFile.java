package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;

import java.util.ArrayList;
import java.util.List;

public class OwnFile {
    public File file;
    private List<OwnFile> folders;
    private List<OwnFile> files;

    public OwnFile(File file) {
        this.file = file;
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public boolean addFolder(OwnFile folder){
        return folders.add(folder);
    }

    public boolean addFile(OwnFile file){
        return files.add(file);
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
