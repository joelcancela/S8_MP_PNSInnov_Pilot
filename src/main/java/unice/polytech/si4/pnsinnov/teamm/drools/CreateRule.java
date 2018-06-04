package unice.polytech.si4.pnsinnov.teamm.drools;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("CreateRule")
public class CreateRule {
    @POST
    public void classifyFiles(@FormParam("extension") String extension,
                              @FormParam("destination-dir") String destinationDir,
                              @FormParam("bonjj") String lol) {
        System.out.println("AAA " + extension);
        System.out.println("BBB " + destinationDir);
        System.out.println("CCC " + lol);
    }
}
