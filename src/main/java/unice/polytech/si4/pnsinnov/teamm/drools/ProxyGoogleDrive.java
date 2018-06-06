package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import unice.polytech.si4.pnsinnov.teamm.api.Login;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;
import unice.polytech.si4.pnsinnov.teamm.persistence.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProxyGoogleDrive {
    private static final Logger logger = LogManager.getLogger(ProxyGoogleDrive.class);

    @PersistenceContext(unitName = "pilot-persistence-unit")
    private EntityManager entityManager;

    public ProxyGoogleDrive() {
    }

    public void applyRules(List<File> files, GDriveSession session, String userID) {
        instantiateRules(userID);
        List<FileInfo> fileInfos = new ArrayList<>();
        FileClassifier fileClassifier = new FileClassifier();

        for (File file : files) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setSession(session);
            fileInfo.setExtension(file.getFileExtension());
            fileInfo.setMimeType(file.getMimeType());
            fileInfo.setNameFile(file.getName().split("\\.")[0]);
            fileInfo.setAcceptedExtensions(fileClassifier.isAcceptedExtension(file.getFileExtension()));
            logger.log(Level.INFO, file.getName() + " is extensions accepted : " + fileInfo.isAcceptedExtensions());
            fileInfo.setAcceptedMimeType(fileClassifier.isAcceptedMimeType(file.getMimeType()));
            logger.log(Level.INFO, file.getName() + " is mimetype accepted : " + fileInfo.isAcceptedMimeType());
            fileInfos.add(fileInfo);
            fileInfo.setFile(file);
        }

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        for (String base : kContainer.getKieBaseNames()) {
            System.out.println(base);
        }

        logger.log(Level.INFO, "RULES TRIGGERED");

        for (FileInfo file : fileInfos) {
            logger.log(Level.INFO, "### Applying rules on the file " + file.getNameFile() + " ###");
            KieSession kSession = kContainer.newKieSession("ksession-file-rules");
            kSession.insert(file);
            kSession.fireAllRules(1);
            kSession.dispose();
        }

    }

    public void instantiateRules(String userID) {
        java.io.File file = new java.io.File("src/main/resources/rules/rulesUse/rules.drl");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file, false));
            out.append("package rules.rulesUse;\n");

            BufferedReader br = new BufferedReader(new FileReader(new java.io.File("src/main/resources/rules/fileRules.drl")));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                out.append(line + "\n");
            }
            br.close();

            //TODO : GET RULES FROM DATABASE
            User user = entityManager.find(User.class, userID);
            List<String> customRules = user.getRules();
            System.out.println("AAAAAAAAAaa "+customRules.size());
            for (String rule : customRules) {
                out.append(rule+"\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
