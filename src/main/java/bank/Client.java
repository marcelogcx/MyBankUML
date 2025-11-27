package bank;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;

public class Client extends User {

    private UserType userType;
    private String fullname;
    private String email;
    private ArrayList<String> bankAccountIds;

    @JsonIgnore
    private Database db;

    @JsonCreator
    public Client(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("email") String email,
            @JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("accountIds") ArrayList<String> bankAccountIds) {
        super(id, username, password);
        this.userType = userType;
        this.fullname = fullname;
        this.email = email;
        this.bankAccountIds = bankAccountIds;
    }

    public void setDatabase(Database db) {
        this.db = db;
    }

    public void linkBankAccount(String bankAccountId) {
        this.bankAccountIds.add(bankAccountId);
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getBankAccountIds() {
        return bankAccountIds;
    }

    /**
     * Logs out of client account
     */
    public void signout() {
        System.out.println("Client " + username + " signed out successfully");
    }

    /**
     * Makes a transaction (transfer) from a bank account owned by this client
     * @param fromAccountId Client's bank account ID
     * @param toAccountId Recipient's bank account ID
     * @param amount Amount to transfer
     * @param description Description of the transaction
     * @return Transfer object if successful, null otherwise
     */
    public Transfer makeTransaction(String fromAccountId, String toAccountId, double amount, String description) {
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

        account.setDatabase(db);
        return account.makeTransaction(toAccountId, amount, description);
    }

    /**
     * Makes a withdrawal from a bank account owned by this client
     * @param accountId Client's bank account ID
     * @param amount Amount to withdraw
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

        account.setDatabase(db);
        return account.makeWithdrawal(amount, description);
    }



    @Override
    public String toString() {
        return id + " " + fullname + " " + email + " " + username + " " + password + " " + bankAccountIds;
    }
}
