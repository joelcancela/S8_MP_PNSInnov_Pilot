package unice.polytech.si4.pnsinnov.teamm.drools;

import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path("CreateRule")
public class CreateRule {
    @POST
    public void addRule(@Context HttpServletRequest request,
                        @Context HttpServletResponse response,
                        @FormParam("options") String options,
                        @FormParam("extension") String extension,
                        @FormParam("regex") String regex,
                        @FormParam("destination-dir") String destinationDir,
                        @FormParam("mimeTypeResult") String mimeTypeResult,
                        @FormParam("regexMode") String regexMode) {

        GDriveSession session = Login.retrieveDriveSessionFromCookie(request);
        if (session == null) {
            try {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
            if (toCompare != null
                    && destinationDir != null
                    && conditionParameter != null
                    && !destinationDir.equals("_NoRuleApplied")
                    && !destinationDir.equals("_Automatic")) {
                Rule rule = new Rule(createRuleName(options, toCompare), toCompare, destinationDir, conditionParameter);
                if (options.equals("patternButton")) {
                    rule.addRuleToSystem(rule.conditionRegexAsDRL());
                } else {
                    rule.addRuleToSystem(rule.conditionAsDRL());
                }
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
