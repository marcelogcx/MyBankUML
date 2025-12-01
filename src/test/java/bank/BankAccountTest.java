package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class BankAccountTest {

    private Database db;
    private BankAccount account;

    @Before
    public void setUp() {
        db = new Database();

      
        String[] accData = {
                "Account A",
                BankAccountType.CHECKING.name(),
                "300.0"
        };

        account = db.writeRecord(BankAccount.class, accData);
        account.setDatabase(db);
    }

    // Get balance
    @Test
    public void getBalance_returnsCurrentBalance() {
        double balance = account.getBalance();
        assertEquals(300.0, balance, 0.0001);
    }

    // Make Deposit
    @Test
    public void makeDeposit_increasesBalance() {
        double original = account.getBalance();

        Deposit deposit = account.makeDeposit(50.0, "JUnit deposit");

        assertNotNull("Deposit operation should be created", deposit);
        assertEquals("Balance should increase by 50",
                original + 50.0, account.getBalance(), 0.0001);
    }

    // Make Withdrawal
    @Test
    public void makeWithdrawal_decreasesBalance_whenEnoughFunds() {
        double original = account.getBalance();

        Withdrawal withdrawal = account.makeWithdrawal(40.0, "JUnit withdrawal");

        assertNotNull("Withdrawal operation should be created", withdrawal);
        assertEquals("Balance should decrease by 40",
                original - 40.0, account.getBalance(), 0.0001);
    }

    // Make Transaction
    @Test
    public void makeTransaction_movesMoneyBetweenTwoAccounts() {
        // Create second account B
        String[] accData2 = {
                "Account B",
                BankAccountType.SAVINGS.name(),
                "100.0"
        };
        BankAccount accountB = db.writeRecord(BankAccount.class, accData2);
        accountB.setDatabase(db);

        double fromOriginal = account.getBalance();   // A
        double toOriginal = accountB.getBalance();    // B

        Transfer transfer = account.makeTransaction(accountB.getId(),
                50.0, "JUnit transfer");

        assertNotNull("Transfer should be created", transfer);
        assertEquals(fromOriginal - 50.0, account.getBalance(), 0.0001);
        assertEquals(toOriginal + 50.0, accountB.getBalance(), 0.0001);
    }
}
