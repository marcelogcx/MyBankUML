package bank;

import core.Database;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a registered teller with select permissions.
 * High Cohesion: Operations conducted by a bank teller for clients.
 * Low Coupling: Only interacts with clients and their account details as needed.
 */
public class Teller extends User {

    private Database db;

    @JsonCreator
    public Teller(@JsonProperty("tellerId") String tellerId,
                  @JsonProperty("username") String username,
                  @JsonProperty("password") String password) {
        super(tellerId, username, password);
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    /**
     * Creates account for client
     * @param userType Type of user account to create
     * @param fullname Client's full name
     * @param email Client's email
     * @param username Client's username
     * @param password Client's password
     * @return Created Client object or null if failed
     */
    public Client register(UserType userType, String fullname, String email, String username, String password) {
        if (db == null) {
            System.err.println("Database not initialized");
            return null;
        }
        if (db.usernameExists(username)) {
            System.err.println("Username already exists");
            return null;
        }
        String[] recordData = {fullname, email, username, password};
        Client newClient = db.writeUser(userType, recordData);
        if (newClient != null) {
            System.out.println("Teller " + this.username + " registered new client: " + username);
        }
        return newClient;
    }

    /**
     * Logs out of the teller account
     */
    public void signout() {
        System.out.println("Teller " + username + " signed out successfully");
    }

    /**
     * Observes client's account information details
     * @param clientId ID of the client to view
     * @return Client object or null if not found
     */
    public Client viewClient(String clientId) {
        if (db == null) {
            System.err.println("Database not initialized");
            return null;
        }
        Client client = db.readRecord(Client.class, clientId);
        if (client != null) {
            System.out.println("Teller " + this.username + " viewing client: " + client.getUsername());
        } else {
            System.err.println("Client not found: " + clientId);
        }
        return client;
    }

    /**
     * Conducts a withdrawal on behalf of the client
     * @param clientId ID of the client
     * @param accountId ID of the bank account
     * @param amount Amount to withdraw
     * @param description Description of the withdrawal
     * @return Withdrawal object if successful, null otherwise
     */
    public Withdrawal makeWithdrawal(String clientId, String accountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized");
            return null;
        }

        Client client = db.readRecord(Client.class, clientId);
        if (client == null) {
            System.err.println("Client not found");
            return null;
        }

        if (!client.getBankAccountIds().contains(accountId)) {
            System.err.println("Account " + accountId + " does not belong to client " + clientId);
            return null;
        }

        BankAccount account = db.readRecord(BankAccount.class, accountId);
        if (account == null) {
            System.err.println("Bank account not found");
            return null;
        }

        account.setDatabase(db);
        Withdrawal withdrawal = account.makeWithdrawal(amount, description);
        if (withdrawal != null) {
            System.out.println("Teller " + this.username + " processed withdrawal for client " + client.getUsername());
        }
        return withdrawal;
    }

    /**
     * Conducts a deposit on behalf of the client
     * @param clientId ID of the client
     * @param accountId ID of the bank account
     * @param amount Amount to deposit
     * @param description Description of the deposit
     * @return Deposit object if successful, null otherwise
     */
    public Deposit makeDeposit(String clientId, String accountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized");
            return null;
        }

        Client client = db.readRecord(Client.class, clientId);
        if (client == null) {
            System.err.println("Client not found");
            return null;
        }

        if (!client.getBankAccountIds().contains(accountId)) {
            System.err.println("Account " + accountId + " does not belong to client " + clientId);
            return null;
        }

        BankAccount account = db.readRecord(BankAccount.class, accountId);
        if (account == null) {
            System.err.println("Bank account not found");
            return null;
        }

        account.setDatabase(db);
        Deposit deposit = account.makeDeposit(amount, description);
        if (deposit != null) {
            System.out.println("Teller " + this.username + " processed deposit for client " + client.getUsername());
        }
        return deposit;
    }

    /**
     * Conducts a transfer on behalf of the client
     * @param clientId ID of the client
     * @param fromAccountId ID of the sender's bank account
     * @param toAccountId ID of the recipient's bank account
     * @param amount Amount to transfer
     * @param description Description of the transfer
     * @return Transfer object if successful, null otherwise
     */
    public Transfer makeTransaction(String clientId, String fromAccountId, String toAccountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized");
            return null;
        }

        Client client = db.readRecord(Client.class, clientId);
        if (client == null) {
            System.err.println("Client not found");
            return null;
        }

        if (!client.getBankAccountIds().contains(fromAccountId)) {
            System.err.println("Account " + fromAccountId + " does not belong to client " + clientId);
            return null;
        }

        BankAccount account = db.readRecord(BankAccount.class, fromAccountId);
        if (account == null) {
            System.err.println("Bank account not found");
            return null;
        }

        account.setDatabase(db);
        Transfer transfer = account.makeTransaction(toAccountId, amount, description);
        if (transfer != null) {
            System.out.println("Teller " + this.username + " processed transfer for client " + client.getUsername());
        }
        return transfer;
    }

    // Getters
    public String getTellerId() {
        return id;
    }

    @Override
    public String toString() {
        return "Teller{tellerId='" + id + "', username='" + username + "'}";
    }
}
