package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.creation;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.Rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Path("CreateRule")
public class CreateRule {
    private static final Logger logger = LogManager.getLogger(CreateRule.class);

    @POST
    public Response addRule(@Context HttpServletRequest request,
                            @Context HttpServletResponse response,
                            @FormParam("options") String options,
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
                    toCompare = "^" + regex + ".*";
                    break;
                case "endsWith":
                    conditionParameter = ConditionParameter.REGEX_END;
                    toCompare = ".*" + regex + "$";
                    break;
                case "contains":
                    conditionParameter = ConditionParameter.REGEX_CONTAINS;
                    toCompare = regex;
                    break;
            }
        }
        int numberOfRules = 0;
        if (toCompare != null
                && destinationDir != null
                && conditionParameter != null
                && !destinationDir.equals("_NoRuleApplied")
                && !destinationDir.equals("_Automatic")) {

            Optional<RuleSet> ruleSet = RuleSetSerializer.getRuleSetForUser(Login.retrieverUserIDFromCookie(request));
            if (ruleSet.isPresent()) {
                List<String> temp = ruleSet.get().getRules();
                numberOfRules = temp.size();
                List<Integer> listSalience = new ArrayList<>();
                for (String rule : temp) {
                    Pattern p = Pattern.compile("salience ([0-9]+)");
                    Matcher matcher = p.matcher(rule);
                    if (matcher.find()) {
                        listSalience.add(Integer.parseInt(matcher.group(1)));
                    }
                }
            } else {
                logger.log(Level.INFO, "Could not find user rules");
            }
            Rule rule = new Rule(createRuleName(options, toCompare), toCompare, destinationDir, conditionParameter);
            if (options.equals("patternButton")) {
                rule.addRuleToSystem(Login.retrieverUserIDFromCookie(request), rule.conditionRegexAsDRL(numberOfRules));
            } else {
                rule.addRuleToSystem(Login.retrieverUserIDFromCookie(request), rule.conditionAsDRL(numberOfRules));
            }
        }
        return null;
    }

    String createRuleName(String options, String toCompare) {
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
