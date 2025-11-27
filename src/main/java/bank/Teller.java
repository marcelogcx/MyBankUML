package bank;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;
import core.PanelEventListener;
import gui.panels.mainpanels.TellerMainPanel;

/**
 * Represents a registered teller with select permissions.
 * High Cohesion: Operations conducted by a bank teller for clients.
 * Low Coupling: Only interacts with clients and their account details as
 * needed.
 */
public class Teller extends User {

    public Teller(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname, @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password, @JsonProperty("isUserBlocked") boolean isUserBlocked) {
        super(id, userType, fullname, email, username, password, isUserBlocked);
    }

    @Override
    public JPanel createMainPanel(Database db, PanelEventListener panelEventListener) {
        return new TellerMainPanel(db, panelEventListener, this);
    }

    public User[] getClients(Database db) {
        return db.getClients(this);
    }

    /**
     * Creates account for client
     * 
     * @param userType Type of user account to create
     * @param fullname Client's full name
     * @param email    Client's email
     * @param username Client's username
     * @param password Client's password
     * @return Created Client object or null if failed
     */
    public Client register(UserType userType, String fullname, String email, String username,
            String password) {
        if (getDatabase() == null) {
            System.err.println("Database not initialized");
            return null;
        }
        if (getDatabase().usernameExists(username)) {
            System.err.println("Username already exists");
            return null;
        }
        String[] recordData = { fullname, email, username, password };
        Client newClient = getDatabase().writeRecord(Client.class, recordData);
        if (newClient != null) {
            System.out.println("Teller " + getUsername() + " registered new client: " + username);
        }
        getDatabase().saveFiles();
        return newClient;
    }

    /**
     * Logs out of the teller account
     */
    public void signout() {
        System.out.println("Teller " + getUsername() + " signed out successfully");
    }

    /**
     * Observes client's account information details
     * 
     * @param clientId ID of the client to view
     * @return Client object or null if not found
     */
    public Client viewClient(String clientId) {
        if (getDatabase() == null) {
            System.err.println("Database not provided");
            return null;
        }
        Client client = getDatabase().readRecord(Client.class, clientId);
        if (client != null) {
            System.out.println("Teller " + getUsername() + " viewing client: " + client.getUsername());
        } else {
            System.err.println("Client not found: " + clientId);
        }
        return client;
    }

    /**
     * Conducts a withdrawal on behalf of the client
     * 
     * @param clientId    ID of the client
     * @param accountId   ID of the bank account
     * @param amount      Amount to withdraw
     * @param description Description of the withdrawal
     * @return Withdrawal object if successful, null otherwise
     */
    public Withdrawal makeWithdrawal(Database db, String clientId, String accountId, double amount,
            String description) {
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

        Withdrawal withdrawal = account.makeWithdrawal(db, amount, description);
        if (withdrawal != null) {
            System.out.println("Teller " + getUsername() + " processed withdrawal for client " + client.getUsername());
        }
        return withdrawal;
    }

    /**
     * Conducts a deposit on behalf of the client
     * 
     * @param clientId    ID of the client
     * @param accountId   ID of the bank account
     * @param amount      Amount to deposit
     * @param description Description of the deposit
     * @return Deposit object if successful, null otherwise
     */
    public Deposit makeDeposit(Database db, String clientId, String accountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not provided");
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

        Deposit deposit = account.makeDeposit(db, amount, description);
        if (deposit != null) {
            System.out.println("Teller " + getUsername() + " processed deposit for client " + client.getUsername());
        }
        return deposit;
    }

    /**
     * Conducts a transfer on behalf of the client
     * 
     * @param clientId      ID of the client
     * @param fromAccountId ID of the sender's bank account
     * @param toAccountId   ID of the recipient's bank account
     * @param amount        Amount to transfer
     * @param description   Description of the transfer
     * @return Transfer object if successful, null otherwise
     */
    public Transfer makeTransaction(Database db, String clientId, String fromAccountId, String toAccountId,
            double amount,
            String description) {
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

        Transfer transfer = account.makeTransaction(db, toAccountId, amount, description);
        if (transfer != null) {
            System.out.println("Teller " + getUsername() + " processed transfer for client " + client.getUsername());
        }
        return transfer;
    }

    @Override
    public String toString() {
        return "Teller{tellerId='" + getId() + "', username='" + getUsername() + "'}";
    }
}
