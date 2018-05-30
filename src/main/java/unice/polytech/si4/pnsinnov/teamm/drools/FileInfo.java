package unice.polytech.si4.pnsinnov.teamm.drools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfo {
    private String extension;
    private String mimeType;
    private String nameFile;

    public FileInfo() {
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public void moveFile(String folderName) {
        /*if(!Files.isDirectory(FileSystems.getDefault().getPath(drivePath + folderName))){
            new File(drivePath + folderName).mkdir();
        }
        Path movefrom = FileSystems.getDefault().getPath(drivePath + nameFile);
        Path target = FileSystems.getDefault().getPath(drivePath + folderName + "/" + nameFile);
        try {
            Files.move(movefrom, target);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void printFile() {
        System.out.println("Extension : " + extension + ", MIME type : " + mimeType);
    }
}


