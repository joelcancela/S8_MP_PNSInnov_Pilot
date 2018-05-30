package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;

import java.util.ArrayList;
import java.util.List;

public class OwnFile {
    private File file;
    private List<OwnFile> folders;
    private List<OwnFile> files;
    private boolean folder;

    public OwnFile(File file, boolean isFolder) {
        this.file = file;
        this.folders = new ArrayList<>();
        this.files = new ArrayList<>();
        this.folder = isFolder;
    }
}
