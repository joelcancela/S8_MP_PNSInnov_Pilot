package unice.polytech.si4.pnsinnov.teamm.drools;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("CreateRule")
public class CreateRule {
    @POST
    public void addRule(@FormParam("options") String options,
                              @FormParam("extension") String extension,
                              @FormParam("regex") String regex,
                              @FormParam("destination-dir") String destinationDir,
                              @FormParam("mimeTypeResult") String mimeTypeResult,
                              @FormParam("regexMode") String regexMode) {

        ConditionParameter conditionParameter = null;
        String toCompare = null;
        if (options.equals("extensionButton")) {
            conditionParameter = ConditionParameter.EXTENSION;
            toCompare = extension;
        } else if (options.equals("mimeButton")) {
            conditionParameter = ConditionParameter.MIME_TYPE;
            toCompare = mimeTypeResult;
        } else if (options.equals("patternButton")) {
            switch (regexMode) {
                case "startsWith":
                    conditionParameter = ConditionParameter.REGEX_START;
                    toCompare = "^"+regex;
                    break;
                case "endsWith":
                    conditionParameter = ConditionParameter.REGEX_END;
                    toCompare = regex+"$";
                    break;
                case "contains":
                    conditionParameter = ConditionParameter.REGEX_CONTAINS;
                    toCompare = regex;
                    break;
            }
        }
        if (toCompare != null && destinationDir != null && conditionParameter != null) {
            Rule rule = new Rule(createRuleName(options, toCompare), toCompare, destinationDir, conditionParameter);
            if (options.equals("patternButton")) {
                rule.addRuleToSystem(rule.conditionRegexAsDRL());
            } else {
                rule.addRuleToSystem(rule.conditionAsDRL());
            }
        }
    }

    private String createRuleName(String options, String toCompare){
        StringBuilder nameBuilder = new StringBuilder();
        if (options.equals("extensionButton")) {
            nameBuilder.append("extension");
        } else if (options.equals("mimeButton")) {
            nameBuilder.append("mimeType");
        } else if (options.equals("patternButton")) {
            nameBuilder.append("regex");
        }
        nameBuilder.append("-");
        nameBuilder.append(toCompare);
        return nameBuilder.toString();
    }
}
