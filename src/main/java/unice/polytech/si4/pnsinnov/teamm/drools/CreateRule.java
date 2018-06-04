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
                              @FormParam("destination-dir") String destinationDir) {
        ConditionParameter conditionParameter;
        String toCompare;
        if (options.equals("extensionButton")) {
            conditionParameter = ConditionParameter.EXTENSION;
            toCompare = extension;
        } else if (options.equals("mimeButton")) {
            conditionParameter = ConditionParameter.MIME_TYPE;
            toCompare = "";
        } else {
            conditionParameter = ConditionParameter.REGEX;
            toCompare = regex;
        }
        Rule rule = new Rule("", toCompare, destinationDir, conditionParameter);
        if (options.equals("patternButton")) {
            rule.addRuleToSystem(rule.conditionRegexAsDRL());
        } else {
            rule.addRuleToSystem(rule.conditionAsDRL());
        }
    }
}
