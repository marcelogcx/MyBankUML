package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class TransferTest {

    private Database db;
    private Client client;
    private BankAccount fromAccount;
    private BankAccount toAccount;

    @Before
    public void setUp() {
        db = new Database();

        String[] clientData = { "Client", "client@example.com", "client", "pw" };
        client = db.writeRecord(Client.class, clientData);

        String[] fromData = { client.getId(), "CHECKING", "From", "70.0" };
        String[] toData = { client.getId(), "SAVINGS", "To", "0.0" };
        fromAccount = db.writeRecord(BankAccount.class, fromData);
        toAccount = db.writeRecord(BankAccount.class, toData);
        fromAccount.setDatabase(db);
        toAccount.setDatabase(db);
        client.linkBankAccount(fromAccount.getId());
        client.linkBankAccount(toAccount.getId());
    }

    @Test
    public void validateOperation_returnsTrueForValidTransfer() {
        Transfer t = new Transfer("op1", "test transfer",
                fromAccount.getId(), toAccount.getId(), 30.0,
                java.time.LocalDate.now().toString(), false);
        t.setDatabase(db);

        assertTrue("Transfer with balance 70 and amount 30 should be valid", t.isValidOperation());
    }

    @Test
    public void executeOperation_updatesBothBalances() {
        Transfer t = new Transfer("op2", "exec transfer",
                fromAccount.getId(), toAccount.getId(), 30.0,
                java.time.LocalDate.now().toString(), false);
        t.setDatabase(db);

        t.executeOperation();

        assertEquals(40.0, fromAccount.getBalance(), 0.0001);
        assertEquals(30.0, toAccount.getBalance(), 0.0001);
        assertTrue(t.getIsSuccessfull());
    }
}
