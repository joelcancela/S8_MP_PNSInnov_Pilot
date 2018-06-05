package unice.polytech.si4.pnsinnov.teamm.drools;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("CreateRule")
public class CreateRule {
    @POST
    public void classifyFiles(@FormParam("options") String options,
                              @FormParam("extension") String extension,
                              @FormParam("regex") String regex,
                              @FormParam("destination-dir") String destinationDir,
                              @FormParam("ruleName") String ruleName,
                              @FormParam("mimeTypeResult") String mimeTypeResult) {

        ConditionParameter conditionParameter = null;
        String toCompare = null;
        if (options.equals("extensionButton")) {
            conditionParameter = ConditionParameter.EXTENSION;
            toCompare = extension;
        } else if (options.equals("mimeButton")) {
            conditionParameter = ConditionParameter.MIME_TYPE;
            toCompare = mimeTypeResult;
        } else if (options.equals("patternButton")) {
            conditionParameter = ConditionParameter.REGEX;
            toCompare = regex;
        }
        if (ruleName != null && toCompare != null && destinationDir != null && conditionParameter != null) {
            Rule rule = new Rule(ruleName, toCompare, destinationDir, conditionParameter);
            if (options.equals("patternButton")) {
                rule.addRuleToSystem(rule.conditionRegexAsDRL());
            } else {
                rule.addRuleToSystem(rule.conditionAsDRL());
            }
        }
    }
}
