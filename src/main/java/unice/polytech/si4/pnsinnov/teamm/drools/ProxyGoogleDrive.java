package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.template.model.Condition;
import org.drools.template.model.Rule;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import unice.polytech.si4.pnsinnov.teamm.drive.GDriveSession;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyGoogleDrive {
    private static final Logger logger = LogManager.getLogger(ProxyGoogleDrive.class);

    public ProxyGoogleDrive() {
    }

    public void applyRules(List<File> files, GDriveSession session) {
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

        logger.log(Level.INFO, "RULES TRIGGERED");

        for (FileInfo file : fileInfos) {
            logger.log(Level.INFO, "### Applying rules on the file " + file.getNameFile() + " ###");
            testFile(kContainer, file);
        }

    }

    private void testFile(KieContainer kContainer, FileInfo fileInfo) {
        KieSession kSession = kContainer.newKieSession("ksession-file-rules");
        kSession.insert(fileInfo);
        kSession.fireAllRules(1);
        kSession.dispose();
    }
}
