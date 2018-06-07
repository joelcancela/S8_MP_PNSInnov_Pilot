package unice.polytech.si4.pnsinnov.teamm.persistence;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import unice.polytech.si4.pnsinnov.teamm.exceptions.UnableToRetrieveUserFileException;

import java.io.*;

import org.apache.logging.log4j.Logger;


public class Serializer {

    private static Logger logger = LogManager.getLogger(Serializer.class);

    public static void serializeToJson(User user, File file) throws UnableToRetrieveUserFileException {
        try {
            if ((file.getParentFile().exists() || file.getParentFile().mkdirs()) && file.createNewFile()) {
                FileWriter fw = new FileWriter(file);
                new Gson().toJson(user, fw);
                fw.flush();
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new UnableToRetrieveUserFileException("Unable to serialize file : IOException");
        }
    }

    public static User deserializeFromJson(File file) throws UnableToRetrieveUserFileException {
        try {
            FileReader fw = new FileReader(file);
            return new Gson().fromJson(fw, User.class);
        } catch (FileNotFoundException e) {
            logger.log(Level.ERROR, e.getMessage());
            throw new UnableToRetrieveUserFileException("Unable to deserialize file : file not found");
        }
    }

}
