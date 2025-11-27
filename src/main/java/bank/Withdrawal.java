package bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;

public class Withdrawal extends BankOperation {

    @JsonIgnore
    private Database db;
    @JsonIgnore
    private boolean wasExecuted = false;
    @JsonIgnore
    private static final double MINIMUM_BALANCE = 0.0;

    @JsonCreator
    public Withdrawal(@JsonProperty("id") String id, @JsonProperty("description") String description,
            @JsonProperty("bankAccountId") String bankAccountId,
            @JsonProperty("amount") double amount, @JsonProperty("date") String date,
            @JsonProperty("isSuccessfull") boolean isSuccessfull) {
        super(id, description, bankAccountId, amount, date, OperationType.WITHDRAWAL,
                isSuccessfull);
    }

    /**
     * Sets the database instance for this withdrawal
     * @param db Database instance
     */
    public void setDatabase(Database db) {
        this.db = db;
    }

    /**
     * Confirms sufficient balance and policy compliance.
     * Validates that the withdrawal amount does not exceed the available balance.
     * @return true if valid, false otherwise
     */
    @JsonIgnore
    @Override
    public boolean isValidOperation() {
        if (getAmount() <= 0) {
            System.err.println("Withdrawal amount must be positive");
            return false;
        }

        if (db == null) {
            System.err.println("Database not initialized for withdrawal validation");
            return false;
        }

        BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
        if (account == null) {
            System.err.println("Bank account not found for withdrawal");
            return false;
        }

        // Check if sufficient balance
        if (account.getBalance() < getAmount()) {
            System.err.println("Insufficient balance. Available: $" + account.getBalance() + ", Requested: $" + getAmount());
            return false;
        }

        // Check if withdrawal would violate minimum balance requirement
        if ((account.getBalance() - getAmount()) < MINIMUM_BALANCE) {
            System.err.println("Withdrawal would violate minimum balance requirement");
            return false;
        }

        return true;
    }

    /**
     * Deducts the withdrawal amount and updates the balance.
     */
    @Override
    public void executeOperation() {
        if (!isValidOperation()) {
            System.err.println("Cannot execute invalid withdrawal operation");
            return;
        }

        if (db == null) {
            System.err.println("Database not initialized for withdrawal execution");
            return;
        }

        BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
        if (account == null) {
            System.err.println("Bank account not found during withdrawal execution");
            return;
        }

        // Deduct from balance (negative amount)
        account.adjustBalance(-getAmount());

        // Link operation to account
        account.linkOperationId(getId());

        wasExecuted = true;
        System.out.println("Withdrawal of $" + getAmount() + " executed successfully");
    }

    /**
     * Records withdrawal in the database history.
     */
    @Override
    public void record() {
        if (!wasExecuted) {
            System.err.println("Cannot record withdrawal that was not executed");
            return;
        }

        if (db != null) {
            db.saveFiles();
            System.out.println("Withdrawal recorded: " + getId());
        } else {
            System.err.println("Database not available for recording withdrawal");
        }
    }

    /**
     * Cancels the operation if any errors occur during the withdrawal (e.g. insufficient balance).
     */
    @Override
    public void cancel() {
        System.out.println("Withdrawal operation cancelled: " + getId());
        // If was executed, reverse the operation
        if (wasExecuted && db != null) {
            BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
            if (account != null) {
                account.adjustBalance(getAmount());
                wasExecuted = false;
                System.out.println("Withdrawal reversed");
            }
        }
    }

    /**
     * Retrieves details about a specific withdrawal
     */
    @Override
    public void getOperationDetails() {
        System.out.println("=== Withdrawal Operation Details ===");
        System.out.println("Operation ID: " + getId());
        System.out.println("Bank Account ID: " + getBankAccountId());
        System.out.println("Amount: $" + String.format("%.2f", getAmount()));
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate());
        System.out.println("Status: " + (getIsSuccessfull() ? "Successful" : "Failed"));
        System.out.println("Operation Type: " + getOperationType());
        System.out.println("Minimum Balance Requirement: $" + String.format("%.2f", MINIMUM_BALANCE));
        System.out.println("====================================");
    }
}
