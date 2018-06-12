package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule;

import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnableToRetrieveUserFileException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("UpdateRuleOrder")
public class UpdateRuleOrder {
    @POST
    public void updateRuleOrder(@Context HttpServletRequest request,
                                @Context HttpServletResponse response,
                                @FormParam("ruleNames") String ruleNamesString) {
        if (ruleNamesString != null) {
            String[] ruleNames = ruleNamesString.split(",");
            Optional<RuleSet> ruleSet = RuleSetSerializer.getRuleSetForUser(Login.retrieverUserIDFromCookie(request));
            List<String> newRuleOrder = new ArrayList<>();
            if (ruleSet.isPresent()) {
                int i = ruleNames.length;
                for (String ruleName : ruleNames) {
                    System.out.println("Name to match : " + ruleName);
                    for (String rule : ruleSet.get().getRules()) {
                        Pattern p = Pattern.compile("\"(.*)\"");
                        Matcher matcher = p.matcher(rule);
                        if (matcher.find()) {
                            System.out.println("Title found : " + matcher.group(1));
                            if (matcher.group(1).equals(ruleName)) {
                                newRuleOrder.add(rule.replaceAll("salience ([0-9]+)", "salience " + Integer.toString(i)));
                                break;
                            }
                        }
                    }
                    i--;
                }
                try {
                    RuleSetSerializer.serializeToJson(new RuleSet.RuleSetBuilder().setUserId(ruleSet.get().getUserId()).setRules(newRuleOrder).build());
                } catch (UnableToRetrieveUserFileException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
