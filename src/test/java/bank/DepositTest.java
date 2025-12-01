package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class DepositTest {

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
    public void executeOperation_increasesBalance() {
        Deposit d = new Deposit("d1", "deposit", account.getId(), 20.0,
                java.time.LocalDate.now().toString(), false);
        d.setDatabase(db);
        d.executeOperation();

        assertEquals(120.0, account.getBalance(), 0.0001);
        assertTrue(d.getIsSuccessfull());
    }
}

