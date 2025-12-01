package bank;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class AdminTest {

    private Database db;
    private Admin admin;

    @Before
    public void setUp() {
        db = new Database();

        // Password must be:
        // - at least 8 chars
        // - contain 1 uppercase and 1 special character
        String[] adminData = {
                "Admin User",
                "admin@example.com",
                "admin",          
                "Admin123!" 
        };

        admin = db.writeRecord(Admin.class, adminData);
        admin.setDatabase(db);
    }

    // 1. Login
    @Test
    public void login_withValidCredentials_givesAccessToAdmin() {
        assertTrue("Username should exist", db.usernameExists("admin"));

        String id = db.getIdFromUsername("admin");
        User loaded = db.readRecord(User.class, id);

        assertNotNull("Admin should be found in DB", loaded);
        assertEquals("Admin123!", loaded.getPassword());
        assertEquals(UserType.ADMIN, loaded.getUserType());
    }

    // 2. Register User
    @Test
    public void registerUser_createsNewClientInDatabase() {
        User created = admin.register(
                UserType.CLIENT,
                "Client Name",
                "client@example.com",
                "clientuser",    
                "Client123!"     
        );

        assertNotNull("Client should be created", created);
        assertTrue("Created user must be a Client", created instanceof Client);

        User fromDb = db.readRecord(User.class, created.getId());
        assertNotNull("Client must be persisted in DB", fromDb);
        assertEquals("clientuser", fromDb.getUsername());
        assertEquals(UserType.CLIENT, fromDb.getUserType());
    }

    // 3. Sign out
    @Test
    public void signout_doesNotThrow() {
        admin.signout();
    }

    // 4. View Account
    @Test
    public void viewAccount_returnsAccurateClientAccountDetails() {
        // Create a client with valid credentials
        String[] clientData = {
                "View Client",
                "viewclient@example.com",
                "viewclient",     
                "ClientPwd!"     
        };
        Client client = db.writeRecord(Client.class, clientData);
        client.setDatabase(db);

        // Create a bank account for this client
        String[] accountData = {
                "Savings Main",
                BankAccountType.SAVINGS.name(),
                "1000.0"
        };
        BankAccount account = db.writeRecord(BankAccount.class, accountData);

        // Link account to client
        client.linkBankAccount(account.getId());

        // From admin's perspective: admin can see users and then view their accounts
        User[] visibleUsers = admin.getUsers(db);
        boolean clientVisible = false;
        for (User u : visibleUsers) {
            if (u.getId().equals(client.getId())) {
                clientVisible = true;
                break;
            }
        }
        assertTrue("Client should be visible to admin", clientVisible);

        // Use Database helper to view the client's accounts
        List<BankAccount> accounts = db.getClientBankAccounts(client.getBankAccountIds());
        assertEquals("Client should have exactly one account", 1, accounts.size());

        BankAccount fromDb = accounts.get(0);
        assertEquals(account.getId(), fromDb.getId());
        assertEquals("Savings Main", fromDb.getAccountName());
        assertEquals(BankAccountType.SAVINGS, fromDb.getBankAccountType());
        assertEquals(1000.0, fromDb.getBalance(), 0.0001);
    }

    // 5. Grant Access
    @Test
    public void grantAccess_changesUserType() {
        String[] userData = {
                "Basic User",
                "user@example.com",
                "basicuser",    
                "UserPass!"    
        };
        Client client = db.writeRecord(Client.class, userData);

        boolean ok = admin.grantAccess(db, client.getId(), UserType.TELLER);
        assertTrue("Grant access should succeed", ok);

        // After grantAccess, the old Client record is deleted and a new
        // Teller record is written with the same username.
        String updatedId = db.getIdFromUsername("basicuser");
        User updated = db.readRecord(User.class, updatedId);

        assertNotNull("Updated user should exist", updated);
        assertEquals(UserType.TELLER, updated.getUserType());
    }
}
