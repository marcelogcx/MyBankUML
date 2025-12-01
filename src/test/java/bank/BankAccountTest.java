package bank;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import core.Database;

public class BankAccountTest {

    private Database db;
    private Client client;
    private BankAccount account;

    @Before
    public void setUp() {
        db = new Database();

        // create one client
        String[] userData = { "Test User", "test@example.com", "testuser", "secret" };
        client = db.writeRecord(Client.class, userData);
        db.saveFiles();

        // create one bank account for the client
        String[] accData = { client.getId(), "CHECKING", "Main account", "300.0" };
        account = db.writeRecord(BankAccount.class, accData);
        account.setDatabase(db);
        db.saveFiles();

        // link account to client
        client.linkBankAccount(account.getId());
    }

    @Test
    public void getBalance_returnsCurrentBalance() {
        double balance = account.getBalance();
        assertEquals(300.0, balance, 0.0001);
    }

    @Test
    public void makeDeposit_increasesBalance() {
        double original = account.getBalance();

        Deposit deposit = account.makeDeposit(50.0, "JUnit deposit");

        assertNotNull("Deposit operation should be created", deposit);
        assertEquals("Balance should increase by 50", original + 50.0, account.getBalance(), 0.0001);
    }

    @Test
    public void makeWithdrawal_decreasesBalance_whenEnoughFunds() {
        double original = account.getBalance();

        Withdrawal withdrawal = account.makeWithdrawal(40.0, "JUnit withdrawal");

        assertNotNull("Withdrawal operation should be created", withdrawal);
        assertEquals("Balance should decrease by 40", original - 40.0, account.getBalance(), 0.0001);
    }

    @Test
    public void makeTransaction_movesMoneyBetweenTwoAccounts() {
        // create second account
        String[] accData2 = { client.getId(), "SAVINGS", "Savings", "100.0" };
        BankAccount accountB = db.writeRecord(BankAccount.class, accData2);
        accountB.setDatabase(db);
        db.saveFiles();

        double fromOriginal = account.getBalance();   // A
        double toOriginal = accountB.getBalance();    // B

        Transfer transfer = account.makeTransaction(accountB.getId(), 50.0, "JUnit transfer");
        assertNotNull("Transfer should be created", transfer);

        assertEquals(fromOriginal - 50.0, account.getBalance(), 0.0001);
        assertEquals(toOriginal + 50.0, accountB.getBalance(), 0.0001);
    }
}
