package unice.polytech.si4.pnsinnov.teamm.drive;

import org.junit.Before;
import org.junit.Test;
import unice.polytech.si4.pnsinnov.teamm.api.OwnFile;

import java.io.IOException;

import static org.junit.Assert.*;

public class GDriveTest {

    private GDrive gDrive;

    @Before
    public void setUp() throws Exception {
        gDrive = new GDrive();
        gDrive.initialize();
        gDrive.subscribeToChanges();
    }

    @Test
    public void classifyFiles() {
        try {
            OwnFile ownFile = gDrive.classifyFiles();
            System.out.println(ownFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}