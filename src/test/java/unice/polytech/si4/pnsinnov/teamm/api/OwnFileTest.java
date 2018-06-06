package unice.polytech.si4.pnsinnov.teamm.api;

import com.google.api.services.drive.model.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OwnFileTest {

    private OwnFile ownFile;

    @BeforeEach
    public void setUp() throws Exception {
        File file = new File();
        ownFile = new OwnFile(file);
    }

    @Test
    public void initTest() throws Exception {
        assertEquals(0, ownFile.getFiles().size());
        assertEquals(0, ownFile.getFolders().size());
    }

    @Test
    public void addFolderTest() throws Exception {
        OwnFile folder = new OwnFile(new File());
        ownFile.addFolder(folder);
        assertEquals(1, ownFile.getFolders().size());
        assertTrue(ownFile.getFolders().contains(folder));
    }

    @Test
    public void addTrashedFolderTest() throws Exception {
        OwnFile trashedFolder = new OwnFile(new File().setTrashed(true));
        ownFile.addFolder(trashedFolder);
        assertEquals(1, ownFile.getFolders().size());
        assertTrue(ownFile.getFolders().contains(trashedFolder));
    }

    @Test
    public void addAlreadyExistingFolderTest() throws Exception {
        OwnFile folder = new OwnFile(new File());
        ownFile.addFolder(folder);
        assertEquals(1, ownFile.getFolders().size());
        assertTrue(ownFile.getFolders().contains(folder));
        ownFile.addFolder(folder);
        assertEquals(2, ownFile.getFolders().size());
    }

    @Test
    public void metaFolderTest() throws Exception {
        OwnFile folder = new OwnFile(new File());
        folder.addFolder(folder);
        assertEquals(1, folder.getFolders().size());
    }
    @Test
    public void addFileTest() throws Exception {
        OwnFile file = new OwnFile(new File());
        ownFile.addFile(file);
        assertEquals(1, ownFile.getFiles().size());
        assertTrue(ownFile.getFiles().contains(file));
    }

    @Test
    public void addTrashedFileTest() throws Exception {
        OwnFile trashedFile = new OwnFile(new File().setTrashed(true));
        ownFile.addFile(trashedFile);
        assertEquals(1, ownFile.getFiles().size());
        assertTrue(ownFile.getFiles().contains(trashedFile));
    }

    @Test
    public void addAlreadyExistingFileTest() throws Exception {
        OwnFile file = new OwnFile(new File());
        ownFile.addFile(file);
        assertEquals(1, ownFile.getFiles().size());
        assertTrue(ownFile.getFiles().contains(file));
        ownFile.addFile(file);
        assertEquals(2, ownFile.getFiles().size());
    }

    @Test
    public void metaFileTest() throws Exception {
        OwnFile file = new OwnFile(new File());
        file.addFile(file);
        assertEquals(1, file.getFiles().size());
    }

    @Test
    public void emptyFileName() throws Exception {
        OwnFile file = new OwnFile(new File());
        assertEquals("", file.toString());
    }

}