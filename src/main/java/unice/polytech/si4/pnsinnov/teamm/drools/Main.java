package unice.polytech.si4.pnsinnov.teamm.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileExplorer fileExplorer = new FileExplorer("C:\\Users\\user\\Desktop\\DriveTest");
        List<FileInfo> files = fileExplorer.getAllFiles();

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kSession = kContainer.newKieSession("ksession-file-rules");

        System.out.println("Rules to check MIME Type = application/pdf and extension = pdf");

        for (FileInfo file : files) {
            System.out.println("Tested file : "+file.getNameFile());
            testFile(kSession, kContainer, file);
        }

    }

    public static void testFile(KieSession kSession, KieContainer kContainer, FileInfo fileInfo){
        kSession = kContainer.newKieSession("ksession-file-rules");
        fileInfo.printFile();
        kSession.insert(fileInfo);
        kSession.fireAllRules();
        kSession.dispose();
    }
}
