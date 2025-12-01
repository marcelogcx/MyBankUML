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

        // teller
        String[] tellerData = { "Teller User", "teller@example.com", "teller", "pass" };
        teller = db.writeRecord(Teller.class, tellerData);
        teller.setDatabase(db);

        // client
        String[] clientData = { "Client User", "client@example.com", "client", "pass" };
        client = db.writeRecord(Client.class, clientData);
        client.setDatabase(db);

     // accounts
        String[] acc1 = { client.getId(), "CHECKING", "A", "300.0" };
        String[] acc2 = { client.getId(), "SAVINGS", "B", "100.0" };
        accountA = db.writeRecord(BankAccount.class, acc1);
        accountB = db.writeRecord(BankAccount.class, acc2);
        accountA.setDatabase(db);
        accountB.setDatabase(db);

        // link accounts properly
        client.linkBankAccount(accountA.getId());
        client.linkBankAccount(accountB.getId());

        db.saveFiles();

    }

    @Test
    public void login_withValidCredentials_givesAccessToTeller() {
        assertTrue(db.usernameExists("teller"));
        String id = db.getIdFromUsername("teller");
        User u = db.readRecord(User.class, id);
        assertNotNull(u);
        assertEquals("pass", u.getPassword());
        assertEquals(UserType.TELLER, u.getUserType());
    }

    @Test
    public void register_addsNewClientToDatabase() {
        Client c = teller.register(UserType.CLIENT,
                "New Client", "new@example.com", "newClient", "pw");
        assertNotNull(c);
        String id = db.getIdFromUsername("newClient");
        assertNotNull("Client must be stored in DB", id);
    }

    @Test
    public void signout_doesNotThrow() {
        teller.signout();
    }

    @Test
    public void viewClient_returnsExistingClient() {
        Client found = teller.viewClient(client.getId());
        assertNotNull(found);
        assertEquals(client.getUsername(), found.getUsername());
    }

    @Test
    public void makeDeposit_increasesClientBalance() {
        double original = accountA.getBalance();
        Deposit dep = teller.makeDeposit(db, client.getId(), accountA.getId(), 50.0, "teller deposit");
        assertNotNull(dep);
        assertEquals(original + 50.0, accountA.getBalance(), 0.0001);
    }

    @Test
    public void makeWithdrawal_decreasesClientBalance() {
        double original = accountA.getBalance();
        Withdrawal w = teller.makeWithdrawal(db, client.getId(), accountA.getId(), 40.0, "teller withdrawal");
        assertNotNull(w);
        assertEquals(original - 40.0, accountA.getBalance(), 0.0001);
    }

    @Test
    public void makeTransaction_movesBalancesCorrectly() {
        double a0 = accountA.getBalance();
        double b0 = accountB.getBalance();

        Transfer t = teller.makeTransaction(db, client.getId(), accountA.getId(), accountB.getId(),
                50.0, "teller transfer");
        assertNotNull(t);

        assertEquals(a0 - 50.0, accountA.getBalance(), 0.0001);
        assertEquals(b0 + 50.0, accountB.getBalance(), 0.0001);
    }
}
