package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnableToRetrieveUserFileException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.creation.ConditionParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Rule {
	private String name;
	private String toCompare;
	private String destinationFolder;
	private ConditionParameter conditionParameter;
	private static final Logger logger = LogManager.getLogger(Rule.class);

	/**
	 * Creates a rule
	 *
	 * @param name               rule name
	 * @param toCompare          selection criteria
	 * @param destinationFolder  folder where the files matching the rule are going to be moved
	 * @param conditionParameter type of rule
	 */
	public Rule(String name, String toCompare, String destinationFolder, ConditionParameter conditionParameter) {
		this.name = name;
		this.toCompare = toCompare;
		this.destinationFolder = destinationFolder;
		this.conditionParameter = conditionParameter;
	}

	public String conditionAsDRL(int salience) throws IllegalStateException, IllegalArgumentException {
		StringBuilder drl = new StringBuilder();
		drl.append("\n\nrule \"")
				.append(name)
				.append("\" salience " + salience + "\n")
				.append("when\n")
				.append("    $file:FileInfo(")
				.append(conditionParameter.parameterString)
				.append(" == \"")
				.append(toCompare)
				.append("\")\n")
				.append("then\n")
				.append("    $file.moveFile(\"")
				.append(destinationFolder)
				.append("\");\n")
				.append("end");
		return drl.toString();
	}

	public String conditionRegexAsDRL(int salience) {
		StringBuilder drl = new StringBuilder();
		drl.append("\n\nrule \"")
				.append(name)
				.append("\" salience " + salience + "\n")
				.append("when\n")
				.append("    $file:FileInfo(nameFile ");
		if (conditionParameter.equals(ConditionParameter.REGEX_CONTAINS)) {
			drl.append("contains \"");
		} else {
			drl.append("matches \"");
		}
		drl.append(toCompare)
				.append("\")\n")
				.append("then\n")
				.append("    $file.moveFile(\"")
				.append(destinationFolder)
				.append("\");\n")
				.append("end");
		return drl.toString();
	}

	public void addRuleToSystem(String userID, String ruleContent) {
		Optional<RuleSet> result = RuleSetSerializer.getRuleSetForUser(userID);
		List<String> rules;
		if (result.isPresent()) {
			rules = result.get().getRules();
		} else {
			rules = new ArrayList<>();
		}
		rules.add(ruleContent);
		RuleSet ruleSet = new RuleSet.RuleSetBuilder().setUserId(userID).setRules(rules).build();
		try {
			RuleSetSerializer.serializeToJson(ruleSet);
		} catch (UnableToRetrieveUserFileException e) {
			logger.log(Level.ERROR, e.getMessage());
		}
	}
}
