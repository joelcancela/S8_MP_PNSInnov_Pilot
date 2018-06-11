package unice.polytech.si4.pnsinnov.teamm.encryption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Encryption {

    /**
     * Returns the new name file after encryption / decryption
     *
     * @param fileName   original file name
     * @param encryption true if the file is encryted, false if it is decrypted
     *
     * @return new file name
     */
    protected String getNewName(String fileName, boolean encryption) {
        String nameToMatch = encryption ? fileName : fileName.replace("-crypted", "");
        Pattern pattern = Pattern.compile("(.*)(\\.[^.]*)$");
        Matcher matcher = pattern.matcher(nameToMatch);
        if (matcher.find()) {
            /* File with extension */
            if (matcher.group(1).equals("")) {
                /* File being only an extension (e.g. ".bashrc") */
                return encryption ? matcher.group(2) + "-crypted" : matcher.group(2);
            } else {
                /* "Normal" file (e.g. "file.txt") */
                String prefix = encryption ? matcher.group(1) + "-crypted" : matcher.group(1);
                return prefix + matcher.group(2);
            }
        } else {
            /* File without extension (e.g. "file") */
            return encryption ? nameToMatch + "-crypted" : nameToMatch;
        }
    }

}
