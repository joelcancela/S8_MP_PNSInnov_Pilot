package unice.polytech.si4.pnsinnov.teamm.drools;


import unice.polytech.si4.pnsinnov.teamm.persistence.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class Rule {
    private String name;
    private String toCompare;
    private String destinationFolder;
    private ConditionParameter conditionParameter;

    @PersistenceContext(unitName = "pilot-persistence-unit")
    private EntityManager entityManager;

    public Rule(String name, String toCompare, String destinationFolder, ConditionParameter conditionParameter) {
        this.name = name;
        this.toCompare = toCompare;
        this.destinationFolder = destinationFolder;
        this.conditionParameter = conditionParameter;
    }

    public String conditionAsDRL() throws IllegalStateException, IllegalArgumentException {
        StringBuilder drl = new StringBuilder();
        drl.append("\n\nrule \"")
                .append(name)
                .append("\"\n")
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

    public String conditionRegexAsDRL() {
        StringBuilder drl = new StringBuilder();
        drl.append("\n\nrule \"")
                .append(name)
                .append("\"\n")
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

    public void addRuleToSystem(String ruleContent, String userID) {
        List<String> rules = new ArrayList<>();
        rules.add(ruleContent);
        User user = new User.UserBuilder().setUserId(userID).setRules(rules).build();
        entityManager.persist(user);
    }
}
