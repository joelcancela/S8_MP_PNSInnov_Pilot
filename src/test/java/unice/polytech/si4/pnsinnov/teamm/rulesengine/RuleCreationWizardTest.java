package unice.polytech.si4.pnsinnov.teamm.rulesengine;

import com.google.api.services.drive.model.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unice.polytech.si4.pnsinnov.teamm.rulesengine.rule.creation.RuleCreationWizard;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleCreationWizardTest {

    private RuleCreationWizard ruleCreationWizard;
    private List<File> files;

    @BeforeEach
    void setUp() {
        ruleCreationWizard = new RuleCreationWizard();
        files = new ArrayList <>();
        files.add(new File().setMimeType("application/vnd.google-apps.folder").setName("_NoRuleApplied").setTrashed(false));
        files.add(new File().setMimeType("application/vnd.google-apps.folder").setName("_Automatic").setTrashed(false));
        files.add(new File().setMimeType("application/vnd.google-apps.folder").setName("dirName").setTrashed(false));
        files.add(new File().setMimeType("text/plain").setTrashed(false));
    }

    @Test
    void createFoldersList() {
        List<String> foldersList = new ArrayList <>();
        ruleCreationWizard.createFoldersList(foldersList, files);
        assertEquals(1, foldersList.size());
    }
}