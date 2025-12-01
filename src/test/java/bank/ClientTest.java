package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class ClientTest {

    private Database db;
    private Client client;
    private BankAccount accountA;
    private BankAccount accountB;

    @Before
    public void setUp() {
        db = new Database();

        String[] clientData = { "Client User", "client@example.com", "clientUser", "pass" };
        client = db.writeRecord(Client.class, clientData);
        client.setDatabase(db);

        String[] acc1 = { client.getId(), "CHECKING", "A", "200.0" };
        String[] acc2 = { client.getId(), "SAVINGS", "B", "100.0" };
        accountA = db.writeRecord(BankAccount.class, acc1);
        accountB = db.writeRecord(BankAccount.class, acc2);
        accountA.setDatabase(db);
        accountB.setDatabase(db);

        client.linkBankAccount(accountA.getId());
        client.linkBankAccount(accountB.getId());
        db.saveFiles();
    }

    @Test
    public void login_withValidCredentials_givesAccessToClient() {
        assertTrue(db.usernameExists("clientUser"));
        String id = db.getIdFromUsername("clientUser");
        User u = db.readRecord(User.class, id);
        assertNotNull(u);
        assertEquals("pass", u.getPassword());
        assertEquals(UserType.CLIENT, u.getUserType());
    }

    @Test
    public void signout_doesNotThrow() {
        client.signout();
    }

    @Test
    public void visualizeAccount_readsCorrectInformation() {
        BankAccount fromDb = db.readRecord(BankAccount.class, accountA.getId());
        assertNotNull(fromDb);
        assertEquals(accountA.getId(), fromDb.getId());
        assertEquals(accountA.getBalance(), fromDb.getBalance(), 0.0001);
    }

    @Test
    public void makeDeposit_increasesBalance() {
        double old = accountA.getBalance();
        Deposit d = client.makeDeposit(accountA.getId(), 30.0, "client deposit");
        assertNotNull(d);
        assertEquals(old + 30.0, accountA.getBalance(), 0.0001);
    }

    @Test
    public void makeWithdrawal_decreasesBalance_whenEnoughFunds() {
        double old = accountA.getBalance();
        Withdrawal w = client.makeWithdrawal(accountA.getId(), 20.0, "client withdraw");
        assertNotNull(w);
        assertEquals(old - 20.0, accountA.getBalance(), 0.0001);
    }

    @Test
    public void makeTransaction_movesMoneyBetweenTwoAccounts() {
        double a0 = accountA.getBalance();
        double b0 = accountB.getBalance();

        Transfer t = client.makeTransaction(accountA.getId(), accountB.getId(), 50.0, "client transfer");
        assertNotNull(t);

        assertEquals(a0 - 50.0, accountA.getBalance(), 0.0001);
        assertEquals(b0 + 50.0, accountB.getBalance(), 0.0001);
    }
}
