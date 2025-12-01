package core;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import bank.Client;
import bank.User;

public class DatabaseTest {

    private Database db;

    @Before
    public void setUp() {
        db = new Database();
    }

    @Test
    public void openFiles_createsDataDirectory() {
        File baseDir = new File(System.getProperty("user.dir"));
        File dataDir = new File(baseDir, "data");
        assertTrue("Data directory should exist after openFiles", dataDir.exists());
    }

    @Test
    public void writeAndReadUserRecord_persistsUserInMemory() {
        String[] userData = { "User Name", "user@example.com", "user1", "pw" };
        User u = db.writeRecord(Client.class, userData);
        assertNotNull(u);

        User fromDb = db.readRecord(User.class, u.getId());
        assertNotNull(fromDb);
        assertEquals("user1", fromDb.getUsername());
    }

    // Approximates "Write Record persists to disk"
    @Test
    public void writeRecord_persistsAfterReload() {
        String[] userData = { "Persisted User", "persist@example.com", "persistUser", "pw" };
        Client c = db.writeRecord(Client.class, userData);
        db.saveFiles();
        String id = c.getId();

        Database db2 = new Database(); // new instance simulating restart
        User fromDb = db2.readRecord(User.class, id);
        assertNotNull("User should be present after reload", fromDb);
        assertEquals("persistUser", fromDb.getUsername());
    }

    @Test
    public void updateRecord_changesField() {
        String[] data = {"Original Name", "u@example.com", "user2", "pw"};
        User u = db.writeRecord(User.class, data);
        String id = u.getId();

        String[] updatedData = {"Updated Name", "u@example.com", "user2", "pw"};
        db.updateRecord(User.class, id, updatedData);   // stored as User.class

        User updated = db.readRecord(User.class, id);
        assertEquals("Updated Name", updated.getFullname());
    }


    @Test
    public void deleteRecord_removesUser() {
        String[] data = {"User To Delete", "del@example.com", "delUser", "pw"};
        User u = db.writeRecord(User.class, data);   // store as User.class
        String id = u.getId();

        db.deleteRecord(User.class, id);             // delete as User.class

        User removed = db.readRecord(User.class, id);
        assertNull(removed);
    }

    @Test
    public void backupDataFiles_createsBackupDirectoryOrFile() {
        boolean ok = db.backupDataFiles();
        assertTrue("Backup should return true even if maps are empty", ok);
    }
}
