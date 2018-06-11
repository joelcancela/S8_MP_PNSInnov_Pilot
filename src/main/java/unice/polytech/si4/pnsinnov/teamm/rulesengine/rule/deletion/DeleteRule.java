package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.deletion;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.exceptions.UnableToRetrieveUserFileException;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("deleteRule")
public class DeleteRule {
    private static Logger logger = LogManager.getLogger(DeleteRule.class);

    @GET
    public void deleteRule(@Context HttpServletRequest request,
                           @Context HttpServletResponse response,
                           @QueryParam("ruleSalience") int salience) {
        Optional<RuleSet> ruleSet = RuleSetSerializer.getRuleSetForUser(Login.retrieverUserIDFromCookie(request));
        if (ruleSet.isPresent()) {
            List<String> rules = ruleSet.get().getRules();
            int index = -1;
            for (int i = 0; i < rules.size(); i++) {
                Pattern p = Pattern.compile("salience ([0-9]+)");
                Matcher matcher = p.matcher(rules.get(i));
                if (matcher.find() && Integer.parseInt(matcher.group(1)) == salience) {
                    index = i;
                }
            }
            if (index != -1) {
                rules.remove(index);
                logger.log(Level.INFO, "Rule with salience " + salience + " removed");
                try {
                    RuleSetSerializer.serializeToJson(new RuleSet.RuleSetBuilder().setRules(rules).setUserId(ruleSet.get().getUserId()).build());
                    logger.log(Level.INFO, "Rules updated");
                } catch (UnableToRetrieveUserFileException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
