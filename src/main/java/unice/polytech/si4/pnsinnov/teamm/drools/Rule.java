package unice.polytech.si4.pnsinnov.teamm.drools;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Rule {
    private String name;
    private String toCompare;
    private String destinationFolder;
    private ConditionParameter conditionParameter;
    private static final Logger logger = LogManager.getLogger(Rule.class);

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
        if (conditionParameter.equals(ConditionParameter.REGEX_CONTAINS)){
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

    public void addRuleToSystem(String ruleContent) {
        File file = new File("src/main/resources/rules/fileRules.drl");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file, true));
            out.append(ruleContent);
            out.close();
        } catch (IOException e) {
            logger.log(Level.ERROR,"Could not add rule");
        }
    }
}
