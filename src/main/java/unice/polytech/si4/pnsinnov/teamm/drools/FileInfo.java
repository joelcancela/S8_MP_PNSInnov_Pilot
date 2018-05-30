package unice.polytech.si4.pnsinnov.teamm.drools;


import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import java.io.IOException;

public class FileInfo {
    private File file;
    private String extension;
    private String mimeType;
    private String nameFile;

    public FileInfo() {
    }

    public String getExtension() {
        return extension;
    }

    public void setFile(File file) {
        this.file = file;
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
        /*boolean folderExist = false;
        folderName = folderName + "Folder";
        try {
            FileList result = Login.googleDrive.drive.files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder'")
                    .execute();
            for (File f : result.getFiles()) {
                if (f.getName() == folderName) {
                    folderExist = true;
                    break;
                }
                System.out.println("!!!@@@###Found : " + f.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File folder = null;
        if (!folderExist) {
            File fileMetaData = new File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            try {
                folder = Login.googleDrive.drive.files().create(fileMetaData).setFields("id").execute();
                System.out.println("Folder " + folderName + " created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Retrieve the existing parents to remove
        File fileParents = null;
        try {
            System.out.println("trying getParents on : " + this.file.getName());
            //fileParents = Login.googleDrive.drive.files().get(this.file.getId())
            System.out.println(Login.googleDrive.drive.files().get(this.file.getId()).setFields("parents").execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileParents != null) {
            StringBuilder previousParents = new StringBuilder();
            System.out.println("getParents on : " + fileParents.getName());
            for (String parent : fileParents.getParents()) {
                System.out.println("Parent : " + parent);
                previousParents.append(parent);
                previousParents.append(',');
            }
            // Move the file to the new folder
            try {
                file = Login.googleDrive.drive.files().update(this.file.getId(), null)
                        .setAddParents(folder.getId())
                        .setRemoveParents(previousParents.toString())
                        .setFields("id, parents")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void printFile() {
        System.out.println("Extension : " + extension + ", MIME type : " + mimeType);
    }
}


