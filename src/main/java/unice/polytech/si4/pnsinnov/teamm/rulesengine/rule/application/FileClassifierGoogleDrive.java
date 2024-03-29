package unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.application;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import unice.polytech.si4.pnsinnov.teamm.drive.FileClassifier;
import unice.polytech.si4.pnsinnov.teamm.drive.FileInfo;
import unice.polytech.si4.pnsinnov.teamm.drive.FileRepresentation;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDrive;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GDriveSession;
import unice.polytech.si4.pnsinnov.teamm.drive.gdrive.GoogleFileInfo;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSet;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.persistence.RuleSetSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO: To edit when abstraction will be done
 */
public class FileClassifierGoogleDrive {
    private static final Logger logger = LogManager.getLogger(FileClassifierGoogleDrive.class);

    public FileClassifierGoogleDrive() {
    }

    public FileRepresentation applyRules(List<File> files, GDriveSession session, String userID, boolean simulation) {
        List<GoogleFileInfo> fileInfos = new ArrayList<>();
        FileClassifier fileClassifier = new FileClassifier();
        FileRepresentation treeFiles = null;
        if (simulation){
            try {
                treeFiles = GDrive.getGDrive().buildFileTree(session);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File file : files) {
            GoogleFileInfo fileInfo = new GoogleFileInfo(file.getName().split("\\.")[0]);
            fileInfo.setgDriveSession(session);
            fileInfo.setExtension(file.getFileExtension());
            fileInfo.setMimeType(file.getMimeType());
            fileInfo.setAcceptedExtensions(fileClassifier.isAcceptedExtension(file.getFileExtension()));
            logger.log(Level.INFO, file.getName() + " is extensions accepted : " + fileInfo.isAcceptedExtensions());
            fileInfo.setAcceptedMimeType(fileClassifier.isAcceptedMimeType(file.getMimeType()));
            logger.log(Level.INFO, file.getName() + " is mimetype accepted : " + fileInfo.isAcceptedMimeType());
            fileInfo.setFile(file);
            fileInfos.add(fileInfo);
        }

        KieServices ks = KieServices.Factory.get();
        logger.log(Level.INFO, "RULES TRIGGERED");

        try {
            StringBuilder content = new StringBuilder(new String(Files.readAllBytes(Paths.get("src/main/resources/rules/fileRules.drl")),
                    Charset.forName("UTF-8")));
            Optional<RuleSet> user = RuleSetSerializer.getRuleSetForUser(userID);
            if (user.isPresent()) {
                List<String> customRules = user.get().getRules();
                for (String rule : customRules) {
                    content.append(rule).append("\n");
                }
            }
            String inMemoryDrlFileName = "src/main/resources/rules/rulesUse/rules.drl";
            KieFileSystem kfs = ks.newKieFileSystem();
            kfs.write(inMemoryDrlFileName, ks.getResources().newReaderResource(new StringReader(content.toString()))
                    .setResourceType(ResourceType.DRL));
            KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
            if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
                System.out.println(kieBuilder.getResults().toString());
            }
            KieContainer kContainer = ks.newKieContainer(kieBuilder.getKieModule().getReleaseId());
            KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
            KieBase kbase = kContainer.newKieBase(kbconf);

            for (FileInfo file : fileInfos) {
                logger.log(Level.INFO, "### Applying rules on the file " + file.getName() + " ###");
                KieSession kSession = kbase.newKieSession();
                kSession.setGlobal("simulation", simulation);
                kSession.setGlobal("treeFile", treeFiles);
                kSession.insert(file);
                kSession.fireAllRules(1);
                kSession.dispose();
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, e.getMessage());
        }
        return treeFiles;
    }
}
