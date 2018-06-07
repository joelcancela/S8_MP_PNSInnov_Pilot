package unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnableToRetrieveUserFileException;

import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;

public class RuleSetSerializer {

	private static Logger logger = LogManager.getLogger(RuleSetSerializer.class);
	private static final String USERS_DIRECTORY = "users";

	public static void serializeToJson(RuleSet ruleSet) throws UnableToRetrieveUserFileException {
		File file = Paths.get(USERS_DIRECTORY, ruleSet.getUserId() + ".json").toFile();
		try {
			if ((file.getParentFile().exists() || file.getParentFile().mkdirs())) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file);
				new Gson().toJson(ruleSet, fw);
				fw.flush();
			}
		} catch (IOException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw new UnableToRetrieveUserFileException("Unable to serialize file : IOException");
		}
	}

	public static RuleSet deserializeFromJson(File file) throws UnableToRetrieveUserFileException {
		try {
			FileReader fw = new FileReader(file);
			return new Gson().fromJson(fw, RuleSet.class);
		} catch (FileNotFoundException e) {
			logger.log(Level.ERROR, e.getMessage());
			throw new UnableToRetrieveUserFileException("Unable to deserialize file : file not found");
		}
	}

	public static Optional<RuleSet> getRuleSetForUser(String userId) {
		try {
			File file = Paths.get(USERS_DIRECTORY, userId + ".json").toFile();
			return Optional.of(deserializeFromJson(file));
		} catch (UnableToRetrieveUserFileException e) {
			logger.log(Level.ERROR, e.getMessage()); // TODO: 06/06/2018 handle this properly
		}
		return Optional.empty();
	}

}
