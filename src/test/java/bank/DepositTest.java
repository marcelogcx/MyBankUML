package bank;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class DepositTest {

    private Database db;
    private BankAccount account;

    @Before
    public void setUp() {
        db = new Database();

        // Given an account balance $100
        String[] accData = {
                "Deposit Account",
                BankAccountType.SAVINGS.name(),
                "100.0"
        };
        account = db.writeRecord(BankAccount.class, accData);
        account.setDatabase(db);
    }

    private Deposit createDeposit(double amount) {
        String[] data = {
                "JUnit deposit",                  // description
                account.getId(),                  // bankAccountId
                String.valueOf(amount),           // amount
                LocalDate.now().toString(),       // date
                "false"                           // isSuccessfull
        };
        Deposit d = db.writeRecord(Deposit.class, data);
        d.setDatabase(db);
        return d;
    }

    // Validate Operation
    @Test
    public void validateOperation_returnsTrueForPositiveAmount() {
        Deposit d = createDeposit(20.0);
        assertTrue("Deposit of a positive amount should be valid",
                d.isValidOperation());
    }

    // Execute Operation
    @Test
    public void executeOperation_updatesBalanceCorrectly() {
        Deposit d = createDeposit(20.0);
        double before = account.getBalance();  // 100

        d.executeOperation();                  // should add 20

        assertEquals("Balance should be N+M = 120",
                before + 20.0, account.getBalance(), 0.0001);
    }

    // Record
    @Test
    public void record_savesDepositInDatabase() {
        Deposit d = createDeposit(20.0);
        d.executeOperation();
        d.record();

        // Operations are stored as BankOperation in the DB
        BankOperation op = db.readRecord(BankOperation.class, d.getId());
        assertNotNull("Deposit should be present in the database", op);
        assertTrue("Stored operation should be a Deposit", op instanceof Deposit);
    }

    // Cancel
    @Test
    public void cancel_restoresAccountBalance() {
        Deposit d = createDeposit(20.0);
        double original = account.getBalance();  // 100

        d.executeOperation();                    // -> 120
        d.cancel();                              // -> back to 100

        assertEquals("Balance should be restored after cancel",
                original, account.getBalance(), 0.0001);
    }

    // Get Operation Details
    @Test
    public void getOperationDetails_returnsCorrectInformation() {
        Deposit d = createDeposit(20.0);
        d.executeOperation();
        d.record();

        d.getOperationDetails();

        assertEquals(20.0, d.getAmount(), 0.0001);
        assertEquals(account.getId(), d.getBankAccountId());
        assertEquals("DEPOSIT", d.getOperationType());
    }
}
