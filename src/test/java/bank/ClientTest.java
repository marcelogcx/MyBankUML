package bank;

import static org.junit.Assert.*;

import java.util.List;

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

        // Create client with VALID username/password 
        String[] clientData = {
                "Client User",
                "client@example.com",
                "clientuser",    
                "Client123!"     
        };
        client = db.writeRecord(Client.class, clientData);
        client.setDatabase(db);

        // Create two accounts
        String[] accountAData = {
                "Account A",
                BankAccountType.CHECKING.name(),
                "300.0"
        };
        accountA = db.writeRecord(BankAccount.class, accountAData);

        String[] accountBData = {
                "Account B",
                BankAccountType.SAVINGS.name(),
                "100.0"
        };
        accountB = db.writeRecord(BankAccount.class, accountBData);

        // Link accounts to client so operations are allowed
        client.linkBankAccount(accountA.getId());
        client.linkBankAccount(accountB.getId());
    }

    // Log in

    // Given valid client credentials, when logging in,
    // the system gives access to the client account.
    @Test
    public void login_withValidCredentials_givesAccessToClient() {
        assertTrue("Client username should exist", db.usernameExists("clientuser"));

        String id = db.getIdFromUsername("clientuser");
        User loaded = db.readRecord(User.class, id);

        assertNotNull("Client should be found in DB", loaded);
        assertEquals("Client123!", loaded.getPassword());
        assertEquals(UserType.CLIENT, loaded.getUserType());
    }

    // Sign out
    @Test
    public void signout_doesNotThrow() {
        client.signout();
    }

    //Visualize Account
    @Test
    public void visualizeAccount_returnsAccurateAccountInfo() {
        List<BankAccount> accounts =
                db.getClientBankAccounts(client.getBankAccountIds());

        // Client has two accounts linked
        assertEquals(2, accounts.size());

        // Find Account A in the list and verify its details
        BankAccount found = null;
        for (BankAccount a : accounts) {
            if (a.getId().equals(accountA.getId())) {
                found = a;
                break;
            }
        }

        assertNotNull("Account A should be visible to the client", found);
        assertEquals("Account A", found.getAccountName());
        assertEquals(BankAccountType.CHECKING, found.getBankAccountType());
        assertEquals(300.0, found.getBalance(), 0.0001);
    }

    //Make Transaction
    @Test
    public void makeDeposit_increasesBalance() {
        double initial = accountA.getBalance();

        Deposit d = client.makeDeposit(accountA.getId(), 50.0, "client deposit");
        assertNotNull("Deposit should be created", d);

        assertEquals(initial + 50.0, accountA.getBalance(), 0.0001);
    }

    // Make Withdrawal
    @Test
    public void makeWithdrawal_decreasesBalance() {
        double initial = accountA.getBalance();

        Withdrawal w = client.makeWithdrawal(accountA.getId(), 30.0, "client withdrawal");
        assertNotNull("Withdrawal should be created", w);

        assertEquals(initial - 30.0, accountA.getBalance(), 0.0001);
    }

    // Make Deposit
    @Test
    public void makeTransaction_movesMoneyBetweenTwoAccounts() {
        double a0 = accountA.getBalance();
        double b0 = accountB.getBalance();

        Transfer t = client.makeTransaction(accountA.getId(), accountB.getId(),
                                            50.0, "client transfer");
        assertNotNull("Transfer should be created", t);

        assertEquals(a0 - 50.0, accountA.getBalance(), 0.0001);
        assertEquals(b0 + 50.0, accountB.getBalance(), 0.0001);
    }
}
