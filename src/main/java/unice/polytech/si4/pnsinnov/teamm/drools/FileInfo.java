package unice.polytech.si4.pnsinnov.teamm.drools;


import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import unice.polytech.si4.pnsinnov.teamm.api.Login;

import java.io.IOException;
import java.util.List;

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

    public void printParent() {

        System.out.println("FOLDER NAMED : " + this.file.getName());

        try {
            System.out.println("trying getParents on : " + this.file.getName() + " id " + this.file.getId());
            System.out.println("Found parent : " + Login.googleDrive.drive.files().get(this.file.getId()).setFields("parents").execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void moveFile(String folderName) {
        boolean folderExist = false;
        File fileMetaData = null;
        System.out.println("### WORK FOR : " + this.file.getName() + " ###");
        try {
            FileList result = Login.googleDrive.drive.files().list()
                    .setQ("mimeType='application/vnd.google-apps.folder'")
                    .execute();
            for (File f : result.getFiles()) {
                //System.out.println("Checking existance : " + f.getName() + " - " + folderName );
                if (f.getName().equals(folderName)) {
                    fileMetaData = f;
                    System.out.println("Folder exists");
                    folderExist = true;
                }
                //System.out.println("!!!@@@###Found : " + f.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!folderExist) {
            fileMetaData = new File();
            fileMetaData.setName(folderName);
            fileMetaData.setMimeType("application/vnd.google-apps.folder");
            try {
                fileMetaData = Login.googleDrive.drive.files().create(fileMetaData).setFields("id").execute();
                System.out.println("WARNNNIIINNNG : Folder " + folderName + " created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Retrieve the existing parents to remove

        File fileParents = null;
        try {
            System.out.println("trying getParents on : " + this.file.getName() + " id " + this.file.getId());
            fileParents = Login.googleDrive.drive.files().get(this.file.getId()).setFields("parents").execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder previousParents = new StringBuilder();
        if (fileParents != null) {
            List<String> parents = fileParents.getParents();
            if (parents != null) {
                for (String parent : parents) {
                    System.out.println("Parent : " + parent);
                    previousParents.append(parent);
                    previousParents.append(',');
                }
            }
        }
            // Move the file to the new folder
            try {
                System.out.println("Will remove parents : " + previousParents.toString() + " for file : " + this.file.getName());
                System.out.println("Will add    parents : " + fileMetaData.getId() + " for file : " + this.file.getName());
                file = Login.googleDrive.drive.files().update(this.file.getId(), null)
                        .setAddParents(fileMetaData.getId())
                        .setRemoveParents(previousParents.toString())
                        .setFields("id, parents")
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void printFile() {
        System.out.println("Extension : " + extension + ", MIME type : " + mimeType);
    }
}


