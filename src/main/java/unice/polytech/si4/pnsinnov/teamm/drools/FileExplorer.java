package unice.polytech.si4.pnsinnov.teamm.drools;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class FileExplorer {
    private List<FileInfo> fileInfos;
    private File[] files;

    public FileExplorer(String path) {
        this.fileInfos = new ArrayList<>();
        File folder = new File(path);
        this.files = folder.listFiles();
    }

    public FileExplorer(File[] files) {
        this.fileInfos = new ArrayList<>();
        this.files = files;
    }

    public List<FileInfo> getAllFiles() {
        for (int i = 0; i < files.length; i++) {
            FileInfo fileInfo = new FileInfo();
            if (files[i].isFile()) {
                String[] fileFormat = files[i].getName().split("\\.");
                if (fileFormat.length == 2){
                    String extension = fileFormat[1];
                    fileInfo.setExtension(extension);
                }
                String mimeType = URLConnection.guessContentTypeFromName(files[i].getName());
                fileInfo.setMimeType(mimeType);
                fileInfo.setNameFile(files[i].getName());
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }
}
