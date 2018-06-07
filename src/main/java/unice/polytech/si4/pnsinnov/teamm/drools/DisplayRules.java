package unice.polytech.si4.pnsinnov.teamm.drools;

import org.glassfish.jersey.server.mvc.Viewable;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.persistence.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("displayRules")
public class DisplayRules {
    @GET
    public Response getCustomRules(@Context HttpServletRequest request,
                               @Context HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        List<String[]> customRules = new ArrayList<>();
        Optional<User> user = DataBase.find(Login.retrieverUserIDFromCookie(request));
        if (user.isPresent()){
            List<String> temp = user.get().getRules();
            for (String rule : temp) {
                rule = rule.replace("\n", " ");
                rule = rule.replace("when", ": when");
                rule = rule.replace("$file:FileInfo(", "");
                rule = rule.replace(")", "");
                rule = rule.replace("$file.moveFile(", "file moves to ");
                rule = rule.replace("; end", "");
                customRules.add(rule.split(":"));
            }
        }
        map.put("customRules", customRules);
        return Response.ok(new Viewable("/display-rules.jsp", map)).build();
    }
}
