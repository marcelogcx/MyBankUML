package bank;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class WithdrawalTest {

    private Database db;
    private BankAccount account;

    @Before
    public void setUp() {
        db = new Database();

        // Given an account balance $100
        String[] accData = {
                "Main Account",
                BankAccountType.CHECKING.name(),
                "100.0"
        };
        account = db.writeRecord(BankAccount.class, accData);
        account.setDatabase(db);
    }

    /** Helper: create a Withdrawal stored in DB and wired to this db */
    private Withdrawal createWithdrawal(double amount) {
        String[] data = {
                "JUnit withdrawal",                 // description
                account.getId(),                    // bankAccountId
                String.valueOf(amount),             // amount
                LocalDate.now().toString(),         // date
                "false"                             
        };
        Withdrawal w = db.writeRecord(Withdrawal.class, data);
        w.setDatabase(db);
        return w;
    }

    // Validate Operation

    @Test
    public void validateOperation_returnsTrueWhenEnoughBalance() {
        Withdrawal w = createWithdrawal(20.0);   // N = 100, M = 20
        assertTrue("Should be valid when balance >= amount",
                w.isValidOperation());
    }

    @Test
    public void validateOperation_returnsFalseWhenInsufficientBalance() {
        Withdrawal w = createWithdrawal(200.0);  // M > N
        assertFalse("Should be invalid when amount > balance",
                w.isValidOperation());
    }

    // Execute Operation

    @Test
    public void executeOperation_updatesBalanceCorrectly() {
        Withdrawal w = createWithdrawal(20.0);
        double before = account.getBalance();    // 100

        w.executeOperation();                    // should subtract 20

        assertEquals("Balance should be N-M = 80",
                before - 20.0, account.getBalance(), 0.0001);
    }

    // Record

    @Test
    public void record_savesWithdrawalInDatabase() {
        Withdrawal w = createWithdrawal(20.0);
        w.executeOperation();
        w.record();

        // Database stores operations as BankOperation
        BankOperation op = db.readRecord(BankOperation.class, w.getId());
        assertNotNull("Withdrawal should be present in the database", op);
        assertTrue("Stored operation should be a Withdrawal", op instanceof Withdrawal);
    }

    // Cancel

    @Test
    public void cancel_restoresAccountBalance() {
        Withdrawal w = createWithdrawal(20.0);
        double original = account.getBalance();  // 100

        w.executeOperation();                    // -> 80
        w.cancel();                              // -> back to 100

        assertEquals("Balance should be restored after cancel",
                original, account.getBalance(), 0.0001);
    }

    // Get Operation Details

    @Test
    public void getOperationDetails_returnsCorrectInformation() {
        Withdrawal w = createWithdrawal(20.0);
        w.executeOperation();
        w.record();

        w.getOperationDetails();

        assertEquals(20.0, w.getAmount(), 0.0001);
        assertEquals(account.getId(), w.getBankAccountId());
        assertEquals("WITHDRAWAL", w.getOperationType());
    }
}
