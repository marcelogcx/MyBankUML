package core;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import bank.BankAccount;
import bank.BankAccountType;
import bank.Client;
import bank.User;

public class DatabaseTest {

    private static int userCounter = 0;

    private Database db;

    @Before
    public void setUp() {
        db = new Database();  
    }

    private String[] makeClientData(String namePrefix) {
        String username = "dbuser" + (userCounter++);
        String email = username + "@example.com";
        // Valid password
        String password = "Pass!" + username;
        return new String[] { namePrefix + " Name", email, username, password };
    }

    // Open Files
    @Test
    public void openFiles_createsOrLoadsDataDirectory() {
        File baseDir = new File(System.getProperty("user.dir"));
        File dataDir = new File(baseDir, "data");
        assertTrue("Data directory should exist after Database initialization",
                dataDir.exists() && dataDir.isDirectory());
    }

    // Write + Read Record (User)
    @Test
    public void writeRecord_and_readRecord_persistAndReturnUser() {
        String[] data = makeClientData("WriteTest");
        Client created = db.writeRecord(Client.class, data);
        assertNotNull(created);

        String id = created.getId();

        User loaded = db.readRecord(User.class, id);
        assertNotNull("User should be readable after write", loaded);
        assertEquals("Username should match", data[2], loaded.getUsername());
    }

    // Write + Save Files + Reload
    @Test
    public void writeRecord_persistsAcrossDatabaseInstancesAfterSave() {
        String[] data = makeClientData("PersistTest");
        Client created = db.writeRecord(Client.class, data);
        String id = created.getId();

        db.saveFiles();               // persist to disk

        Database db2 = new Database();   // reload from files
        User loaded = db2.readRecord(User.class, id);
        assertNotNull("User should be readable from a new Database instance", loaded);
        assertEquals(data[2], loaded.getUsername());
    }

    // Update Record
    @Test
    public void updateRecord_updatesClientFields() {
        String[] data = makeClientData("UpdateOrig");
        Client created = db.writeRecord(Client.class, data);
        String id = created.getId();

        String[] updated = {
                "Updated Name",  // fullname
                data[1],         // same email
                data[2],         // same username
                data[3]          // same password
        };

        User updatedUser = db.updateRecord(User.class, id, updated);
        assertNotNull("Updated user should not be null", updatedUser);
        assertEquals("Updated Name", updatedUser.getFullname());

        User fromDb = db.readRecord(User.class, id);
        assertEquals("Updated Name", fromDb.getFullname());
    }

    // Delete Record
    @Test
    public void deleteRecord_removesUserFromDatabase() {
        String[] data = makeClientData("DeleteTest");
        Client created = db.writeRecord(Client.class, data);
        String id = created.getId();

        User removed = db.deleteRecord(User.class, id);
        assertNotNull("deleteRecord should return the removed user", removed);

        User afterDelete = db.readRecord(User.class, id);
        assertNull("User should no longer be present after delete", afterDelete);
    }

    // Read Record (BankAccount)
    @Test
    public void readRecord_returnsCorrectBankAccount() {
        String[] accData = {
                "Test Account",
                BankAccountType.CHECKING.name(),
                "50.0"
        };
        BankAccount acc = db.writeRecord(BankAccount.class, accData);
        String id = acc.getId();

        BankAccount fromDb = db.readRecord(BankAccount.class, id);
        assertNotNull("Bank account should be readable", fromDb);
        assertEquals("Test Account", fromDb.getAccountName());
        assertEquals(50.0, fromDb.getBalance(), 0.0001);
    }

    // Backup Data Files
    @Test
    public void backupDataFiles_createsBackupDirectoryAndFiles() {
        boolean ok = db.backupDataFiles();
        assertTrue("backupDataFiles() should return true", ok);

        try {
            java.io.File jarFile = new java.io.File(
                    Database.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            java.io.File baseDir = jarFile.isFile() ? jarFile.getParentFile() : jarFile;
            java.io.File backupDir = new java.io.File(baseDir, "data/backup");

            assertTrue("Backup directory should exist", backupDir.exists() && backupDir.isDirectory());

            File[] files = backupDir.listFiles();
            assertNotNull("Backup directory should list files", files);
            assertTrue("Backup directory should contain at least one backup file", files.length > 0);
        } catch (Exception e) {
            fail("Exception while checking backup directory: " + e.getMessage());
        }
    }
}
