package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class TellerTest {

    private Database db;
    private Teller teller;
    private Client client;
    private BankAccount accountA;
    private BankAccount accountB;

    @Before
    public void setUp() {
        db = new Database();

        // Create a teller with valid credentials
        String[] tellerData = {
                "Teller User",
                "teller@example.com",
                "telleruser",    
                "Teller123!"     
        };
        teller = db.writeRecord(Teller.class, tellerData);
        teller.setDatabase(db);

        // Create a client
        String[] clientData = {
                "Client User",
                "client@example.com",
                "clientuser",
                "Client123!"
        };
        client = db.writeRecord(Client.class, clientData);
        client.setDatabase(db);

        // Create two accounts for the client
        String[] accountAData = {
                "Account A",
                BankAccountType.CHECKING.name(),
                "300.0"            // starting balance for A
        };
        accountA = db.writeRecord(BankAccount.class, accountAData);
        accountA.setDatabase(db);

        String[] accountBData = {
                "Account B",
                BankAccountType.SAVINGS.name(),
                "100.0"            // starting balance for B
        };
        accountB = db.writeRecord(BankAccount.class, accountBData);
        accountB.setDatabase(db);

        // Link accounts to client so teller operations are allowed
        client.linkBankAccount(accountA.getId());
        client.linkBankAccount(accountB.getId());
    }

    // Log in
    @Test
    public void login_withValidCredentials_givesAccessToTeller() {
        assertTrue("Teller username should exist", db.usernameExists("telleruser"));

        String id = db.getIdFromUsername("telleruser");
        User loaded = db.readRecord(User.class, id);

        assertNotNull("Teller should be found in DB", loaded);
        assertEquals("Teller123!", loaded.getPassword());
        assertEquals(UserType.TELLER, loaded.getUserType());
    }

    // Register
    @Test
    public void register_createsNewClientInDatabase() {
        Client created = teller.register(
                UserType.CLIENT,
                "New Client",
                "newclient@example.com",
                "newclient",
                "NewClient1!"
        );

        assertNotNull("Client should be created", created);

        User fromDb = db.readRecord(User.class, created.getId());
        assertNotNull("Client must be persisted in DB", fromDb);
        assertEquals("newclient", fromDb.getUsername());
        assertEquals(UserType.CLIENT, fromDb.getUserType());
    }

    // Sign out
    @Test
    public void signout_doesNotThrow() {
        teller.signout();
    }

    // View Client
    @Test
    public void viewClient_returnsAccurateClientInfo() {
        User[] clients = teller.getClients(db);
        assertNotNull("Teller should receive a client list", clients);

        boolean found = false;
        for (User u : clients) {
            if (u.getId().equals(client.getId())) {
                found = true;
                assertEquals("Client User", u.getFullname());
                assertEquals("clientuser", u.getUsername());
                break;
            }
        }
        assertTrue("Existing client should appear in teller client list", found);
    }

    // Deposit
    @Test
    public void makeDeposit_increasesClientBalance() {
        double initial = accountA.getBalance();

        Deposit d = teller.makeDeposit(db, client.getId(), accountA.getId(),
                50.0, "teller deposit");
        assertNotNull("Deposit operation should be created", d);

        assertEquals(initial + 50.0, accountA.getBalance(), 0.0001);
    }

    // Make Withdrawal
    @Test
    public void makeWithdrawal_decreasesClientBalance() {
        double initial = accountA.getBalance();

        Withdrawal w = teller.makeWithdrawal(db, client.getId(), accountA.getId(),
                30.0, "teller withdrawal");
        assertNotNull("Withdrawal operation should be created", w);

        assertEquals(initial - 30.0, accountA.getBalance(), 0.0001);
    }

    // Make Transaction
    @Test
    public void makeTransaction_movesBalancesCorrectly() {
        double a0 = accountA.getBalance();
        double b0 = accountB.getBalance();

        Transfer t = teller.makeTransaction(db, client.getId(),
                accountA.getId(), accountB.getId(),
                50.0, "teller transfer");
        assertNotNull("Transfer operation should be created", t);

        assertEquals(a0 - 50.0, accountA.getBalance(), 0.0001);
        assertEquals(b0 + 50.0, accountB.getBalance(), 0.0001);
    }
}
