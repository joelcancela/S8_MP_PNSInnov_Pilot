package unice.polytech.si4.pnsinnov.teamm.drools;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.exceptions.UnableToRetrieveUserFileException;
import unice.polytech.si4.pnsinnov.teamm.persistence.Serializer;
import unice.polytech.si4.pnsinnov.teamm.persistence.User;

import java.nio.file.Paths;
import java.util.Optional;

public class DataBase {

    private static final Logger logger = LogManager.getLogger(DataBase.class);

    private static final String USERS_DIRECTORY = "users";

    public static void persist(User user) {
        try {
            Serializer.serializeToJson(user, Paths.get(USERS_DIRECTORY, user.getUserId() + ".json").toFile());
        } catch (UnableToRetrieveUserFileException e) {
            logger.log(Level.ERROR, e.getMessage()); // TODO: 06/06/2018 handle this properly
        }
    }

    public static Optional<User> find(String userId) {
        try {
            return Optional.of(Serializer.deserializeFromJson(Paths.get(USERS_DIRECTORY, userId + ".json").toFile()));
        } catch (UnableToRetrieveUserFileException e) {
            logger.log(Level.ERROR, e.getMessage()); // TODO: 06/06/2018 handle this properly
        }
        return Optional.empty();
    }
}

