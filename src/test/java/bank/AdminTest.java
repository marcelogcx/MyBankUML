package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class AdminTest {

    private Database db;
    private Admin admin;

    @Before
    public void setUp() {
        db = new Database();
        String[] adminData = { "Admin User", "admin@example.com", "admin", "adminpass" };
        admin = db.writeRecord(Admin.class, adminData);
        admin.setDatabase(db);
        db.saveFiles();
    }

    // Login test – matches "Given valid admin credentials ... access to admin account"
    @Test
    public void login_withValidCredentials_givesAccessToAdmin() {
        assertTrue("Username should exist", db.usernameExists("admin"));
        String id = db.getIdFromUsername("admin");
        User loaded = db.readRecord(User.class, id);
        assertNotNull(loaded);
        assertEquals("adminpass", loaded.getPassword());
        assertEquals(UserType.ADMIN, loaded.getUserType());
    }

    // Register user – new client added to DB
    @Test
    public void registerUser_createsNewClientInDatabase() {
        User created = admin.register(UserType.CLIENT,
                "Client Name", "client@example.com", "clientUser", "1234");

        assertNotNull("Client should be created", created);
        assertTrue("Created user must be instance of Client", created instanceof Client);

        User fromDb = db.readRecord(User.class, created.getId());
        assertNotNull("Client must be persisted in DB", fromDb);
        assertEquals("clientUser", fromDb.getUsername());
    }

    // Sign out – just must succeed
    @Test
    public void signout_doesNotThrow() {
        admin.signout();
    }

    // Grant access – change client -> teller
    @Test
    public void grantAccess_changesUserType() {
        String[] userData = { "User", "user@example.com", "basicUser", "pass" };
        Client client = db.writeRecord(Client.class, userData);

        boolean ok = admin.grantAccess(db, client.getId(), UserType.TELLER);
        assertTrue("Grant access should succeed", ok);

        String updatedId = db.getIdFromUsername("basicUser");
        User updated = db.readRecord(User.class, updatedId);

        assertEquals(UserType.TELLER, updated.getUserType());
    }
}
