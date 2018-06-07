package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.display;

import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("displayRules")
public class DisplayRules {
	@GET
	public Response getCustomRules(@Context HttpServletRequest request,
	                               @Context HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		List<List<String>> customRules = new ArrayList<>();
		Optional<RuleSet> ruleSet = RuleSetSerializer.getRuleSetForUser(Login.retrieverUserIDFromCookie(request));
		if (ruleSet.isPresent()) {
			List<String> temp = ruleSet.get().getRules();
			for (String rule : temp) {
				List<String> list = new ArrayList<>();
				Pattern p = Pattern.compile("salience ([0-9]+)");
				Matcher matcher = p.matcher(rule);
				if (matcher.find()) {
					list.add(matcher.group(1));
				} else {
					System.out.println("Regex not found");
				}
				rule = rule.replace("\n", " ");
				rule = rule.replace("when", ": when");
				rule = rule.replace("$file:FileInfo(", "");
				rule = rule.replace(")", "");
				rule = rule.replace("$file.moveFile(", "file moves to ");
				rule = rule.replace("; end", "");
				rule = rule.replace("salience " + list.get(0), "");
				list.addAll(Arrays.asList(rule.split(":")));
				customRules.add(list);
			}
		}
		map.put("customRules", customRules);
		return Response.ok(new Viewable("/display-rules.jsp", map)).build();
	}
}
