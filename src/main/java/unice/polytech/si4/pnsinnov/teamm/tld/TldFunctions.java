package unice.polytech.si4.pnsinnov.teamm.tld;

import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;

import java.util.regex.Pattern;

public class TldFunctions {

    public static boolean isFileCrypted(FileRepresentation file){
        return Pattern.matches(".*-crypted\\..*", file.file.getName());
    }
}
