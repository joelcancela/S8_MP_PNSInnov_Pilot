package unice.polytech.si4.pnsinnov.teamm.drools;

import com.google.api.services.drive.model.File;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

public class ProxyGoogleDrive {


    public ProxyGoogleDrive() {
    }

    public void applyRules(List<File> files) {
        List<FileInfo> fileInfos = new ArrayList<>();

        for (File file : files) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setExtension(file.getFileExtension());
            fileInfo.setMimeType(file.getMimeType());
            fileInfo.setNameFile(file.getName());
            fileInfos.add(fileInfo);
        }

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        System.out.println("Rules to check MIME Type = application/pdf and extension = pdf");

        for (FileInfo file : fileInfos) {
            System.out.println("Tested file : "+file.getNameFile());
            testFile(kContainer, file);
        }

    }

    private void testFile(KieContainer kContainer, FileInfo fileInfo){
        KieSession kSession = kContainer.newKieSession("ksession-file-rules");
        fileInfo.printFile();
        kSession.insert(fileInfo);
        kSession.fireAllRules();
        kSession.dispose();
    }
}