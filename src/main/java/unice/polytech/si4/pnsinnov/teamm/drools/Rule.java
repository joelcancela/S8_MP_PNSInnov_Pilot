package unice.polytech.si4.pnsinnov.teamm.drools;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Rule {
    private String name;
    private String toCompare;
    private String destinationFolder;
    private ConditionParameter conditionParameter;

    public Rule(String name, String toCompare, String destinationFolder, ConditionParameter conditionParameter) {
        this.name = name;
        this.toCompare = toCompare;
        this.destinationFolder = destinationFolder;
        this.conditionParameter = conditionParameter;
    }

    public String conditionAsDRL() throws IllegalStateException, IllegalArgumentException {
        StringBuilder drl = new StringBuilder();
        drl.append("\n\nrule \"" + name + "\"\n");
        drl.append("when\n");
        drl.append("    $file:FileInfo("+conditionParameter.parameterString+" == \""+toCompare+"\")\n");
        drl.append("then\n");
        drl.append("    $file.moveFile(\""+destinationFolder+"\");\n");
        drl.append("end");
        return drl.toString();
    }

    public String conditionRegexAsDRL(){
        StringBuilder drl = new StringBuilder();
        drl.append("\n\nrule \"" + name + "\"\n");
        drl.append("when\n");
        drl.append("    $file:FileInfo("+conditionParameter.parameterString+" matches \""+toCompare+"\")\n");
        drl.append("then\n");
        drl.append("    $file.moveFile(\""+destinationFolder+"\");\n");
        drl.append("end");
        return drl.toString();
    }

    public void addRuleToSystem(String ruleContent){
        File file = new File("src/main/resources/rules/fileRules.drl");
        try{
            PrintWriter out = new PrintWriter(new FileWriter(file, true));
            out.append(ruleContent);
            out.close();
        }catch(IOException e){
            System.out.println("Could not add rule");
        }
    }
}