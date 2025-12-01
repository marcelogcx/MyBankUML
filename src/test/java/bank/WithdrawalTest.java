package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class WithdrawalTest {

    private Database db;
    private Client client;
    private BankAccount account;

    @Before
    public void setUp() {
        db = new Database();

        String[] clientData = { "Client", "client@example.com", "client", "pw" };
        client = db.writeRecord(Client.class, clientData);

        String[] accData = { client.getId(), "CHECKING", "Main", "100.0" };
        account = db.writeRecord(BankAccount.class, accData);
        account.setDatabase(db);
        client.linkBankAccount(account.getId());
    }

    @Test
    public void validateOperation_returnsTrueWhenEnoughFunds() {
        Withdrawal w = new Withdrawal("w1", "withdraw", account.getId(), 20.0,
                java.time.LocalDate.now().toString(), false);
        w.setDatabase(db);

        assertTrue(w.isValidOperation());
    }

    @Test
    public void validateOperation_returnsFalseWhenInsufficientFunds() {
        Withdrawal w = new Withdrawal("w2", "withdraw", account.getId(), 200.0,
                java.time.LocalDate.now().toString(), false);
        w.setDatabase(db);

        assertFalse(w.isValidOperation());
    }

    @Test
    public void executeOperation_updatesBalance() {
        Withdrawal w = new Withdrawal("w3", "withdraw", account.getId(), 20.0,
                java.time.LocalDate.now().toString(), false);
        w.setDatabase(db);
        w.executeOperation();

        assertEquals(80.0, account.getBalance(), 0.0001);
        assertTrue(w.getIsSuccessfull());
    }
}
