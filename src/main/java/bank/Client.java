package bank;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.ClientListener;
import core.Database;
import core.PanelEventListener;
import gui.panels.mainpanels.ClientMainPanel;

public class Client extends User {
    private List<ClientListener> listeners = new ArrayList<>();
    private List<String> bankAccountIds;

    @JsonIgnore
    private Database db;

    @JsonCreator
    public Client(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("email") String email,
            @JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("accountIds") ArrayList<String> bankAccountIds,
            @JsonProperty("isUserBlocked") boolean isUserBlocked) {
        super(id, userType, fullname, email, username, password, isUserBlocked);
        this.bankAccountIds = bankAccountIds;
    }

    public void addListener(ClientListener cl) {
        listeners.add(cl);
    }

    public void linkBankAccount(String bankAccountId) {
        this.bankAccountIds.add(bankAccountId);
        for (ClientListener cl : listeners) {
            cl.onAdditionBankAccount(bankAccountIds);
        }
    }

    public List<String> getBankAccountIds() {
        return bankAccountIds;
    }

    /**
     * Logs out of client account
     */
    public void signout() {
        System.out.println("Client " + getUsername() + " signed out successfully");
    }

    /**
     * Makes a transaction (transfer) from a bank account owned by this client
     * 
     * @param fromAccountId Client's bank account ID
     * @param toAccountId   Recipient's bank account ID
     * @param amount        Amount to transfer
     * @param description   Description of the transaction
     * @return Transfer object if successful, null otherwise
     */
    public Transfer makeTransaction(Database db, String fromAccountId, String toAccountId, double amount,
            String description) {
        if (db == null) {
            System.err.println("Database not initialized for transaction");
            return null;
        }

        if (!bankAccountIds.contains(fromAccountId)) {
            System.err.println("Account " + fromAccountId + " does not belong to this client");
            return null;
        }

        BankAccount account = db.readRecord(BankAccount.class, fromAccountId);
        if (account == null) {
            System.err.println("Bank account not found");
            return null;
        }
        return account.makeTransaction(db, toAccountId, amount, description);
    }

    /**
     * Makes a withdrawal from a bank account owned by this client
     * 
     * @param accountId   Client's bank account ID
     * @param amount      Amount to withdraw
     * @param description Description of the withdrawal
     * @return Withdrawal object if successful, null otherwise
     */
    public Withdrawal makeWithdrawal(String accountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized for withdrawal");
            return null;
        }

        if (!bankAccountIds.contains(accountId)) {
            System.err.println("Account " + accountId + " does not belong to this client");
            return null;
        }

        BankAccount account = db.readRecord(BankAccount.class, accountId);
        if (account == null) {
            System.err.println("Bank account not found");
            return null;
        }

        return account.makeWithdrawal(db, amount, description);
    }

    @Override
    public String toString() {
        return getId() + " " + getFullname() + " " + getEmail() + " " + getUsername() + " " + getPassword() + " "
                + bankAccountIds;
    }

    @Override
    public JPanel createMainPanel(Database db, PanelEventListener panelEventListener) {
        return new ClientMainPanel(db, panelEventListener, this);
    }

}
