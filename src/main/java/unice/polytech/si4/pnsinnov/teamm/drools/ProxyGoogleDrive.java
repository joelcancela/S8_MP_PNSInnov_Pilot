package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

public class ProxyGoogleDrive {
    private static final Logger logger = LogManager.getLogger(ProxyGoogleDrive.class);

    public ProxyGoogleDrive() {
    }

    public void applyRules(List<File> files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        FileClassifier fileClassifier = new FileClassifier();

        for (File file : files) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setExtension(file.getFileExtension());
            fileInfo.setMimeType(file.getMimeType());
            fileInfo.setNameFile(file.getName());
            fileInfo.setAcceptedExtensions(fileClassifier.isAcceptedExtension(file.getFileExtension()));
            logger.log(Level.INFO, file.getName() + " is extensions accepted : " + fileInfo.isAcceptedExtensions());
            fileInfo.setAcceptedMimeType(fileClassifier.isAcceptedMimeType(file.getMimeType()));
            logger.log(Level.INFO, file.getName() + " is mimetype accepted : " + fileInfo.isAcceptedMimeType());
            fileInfos.add(fileInfo);
            fileInfo.setFile(file);
        }

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        logger.log(Level.INFO,"RULES TRIGGERED");

        for (FileInfo file : fileInfos) {
            logger.log(Level.INFO, "### Applying rules on the file " + file.getNameFile() + " ###");
            testFile(kContainer, file);
        }

    }

    private void testFile(KieContainer kContainer, FileInfo fileInfo){
        KieSession kSession = kContainer.newKieSession("ksession-file-rules");
        kSession.insert(fileInfo);
        kSession.fireAllRules(1);
        kSession.dispose();
    }
}
