package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class TransferTest {

    private Database db;
    private BankAccount sender;
    private BankAccount recipient;

    @Before
    public void setUp() {
        db = new Database();

        // Given sender balance = 70, recipient balance = 0
        String[] senderData = { "Sender Account", BankAccountType.CHECKING.name(), "70.0" };
        String[] recipientData = { "Recipient Account", BankAccountType.SAVINGS.name(), "0.0" };

        sender = db.writeRecord(BankAccount.class, senderData);
        recipient = db.writeRecord(BankAccount.class, recipientData);

        sender.setDatabase(db);
        recipient.setDatabase(db);
    }

    private Transfer createTransfer(double amount) {
        String[] recordData = {
                "JUnit transfer",                     // description
                sender.getId(),                       // sender account id
                recipient.getId(),                    // recipient account id
                String.valueOf(amount),               // amount
                java.time.LocalDate.now().toString(), // date
                "false"                               // isSuccessfull
        };
        Transfer t = db.writeRecord(Transfer.class, recordData);
        t.setDatabase(db);
        return t;
    }

    // Validate Operation
    @Test
    public void validateOperation_withExistingAccountsAndEnoughFunds_returnsTrue() {
        Transfer t = createTransfer(30.0);
        assertTrue("Transfer should be valid", t.isValidOperation());
    }

    // Execute Operation
    @Test
    public void executeOperation_updatesBothBalancesCorrectly() {
        Transfer t = createTransfer(30.0);

        t.executeOperation();

        assertEquals(40.0, sender.getBalance(), 0.0001);
        assertEquals(30.0, recipient.getBalance(), 0.0001);
    }

    // Record
    @Test
    public void recordOperation_keepsTransferInDatabase() {
        Transfer t = createTransfer(30.0);
        t.executeOperation();
        t.record();

        BankOperation op = db.readRecord(BankOperation.class, t.getId());
        assertNotNull("Transfer should be present in the database", op);
        assertTrue("Stored operation should be a Transfer", op instanceof Transfer);
    }

    // Cancel
    @Test
    public void cancelOperation_restoresOriginalBalances() {
        Transfer t = createTransfer(30.0);

        double senderBefore = sender.getBalance();      // 70.0
        double recipientBefore = recipient.getBalance(); // 0.0

        t.executeOperation();  // sender 40, recipient 30
        t.cancel();            // should reverse

        assertEquals(senderBefore, sender.getBalance(), 0.0001);
        assertEquals(recipientBefore, recipient.getBalance(), 0.0001);
    }

    // Get Operation Details
    @Test
    public void getOperationDetails_returnsCorrectAmount() {
        Transfer t = createTransfer(30.0);
        t.executeOperation();
        t.record();

        t.getOperationDetails(); // just prints to console

        assertEquals(30.0, t.getAmount(), 0.0001);
    }
}
