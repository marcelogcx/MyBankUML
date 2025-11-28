package bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;

public class Deposit extends BankOperation {

    @JsonIgnore
    private Database db;
    @JsonIgnore
    private boolean wasExecuted = false;

    @JsonCreator
    public Deposit(@JsonProperty("id") String id, @JsonProperty("description") String description,
            @JsonProperty("bankAccountId") String bankAccountId,
            @JsonProperty("amount") double amount, @JsonProperty("date") String date,
            @JsonProperty("isSuccessfull") boolean isSuccessfull) {
        super(id, description, bankAccountId, amount, date, OperationType.DEPOSIT,
                isSuccessfull);
    }

    /**
     * Sets the database instance for this deposit
     * 
     * @param db Database instance
     */
    public void setDatabase(Database db) {
        this.db = db;
    }

    /**
     * Checks if the deposit can be made according to sufficient balance and bank
     * policy.
     * Validates that the deposit amount is positive and complies with bank policy.
     * 
     * @return true if valid, false otherwise
     */
    @JsonIgnore
    @Override
    public boolean isValidOperation() {
        if (getAmount() < 0) {
            System.err.println("Deposit amount must be positive");
            return false;
        }

        if (db == null) {
            System.err.println("Database not initialized for deposit validation");
            return false;
        }

        BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
        if (account == null) {
            System.err.println("Bank account not found for deposit");
            return false;
        }

        return true;
    }

    /**
     * Performs the deposit by adding the amount and updating the balance.
     */
    @Override
    public void executeOperation() {
        if (!isValidOperation()) {
            System.err.println("Cannot execute invalid deposit operation");
            return;
        }

        if (db == null) {
            System.err.println("Database not initialized for deposit execution");
            return;
        }

        BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
        if (account == null) {
            System.err.println("Bank account not found during deposit execution");
            return;
        }

        // Update balance
        account.adjustBalance(getAmount());

        // Link operation to account
        account.linkOperationId(getId());

        wasExecuted = true;
        System.out.println("Deposit of $" + getAmount() + " executed successfully");
    }

    /**
     * Records the deposit in the database history.
     */
    @Override
    public void record() {
        if (!wasExecuted) {
            System.err.println("Cannot record deposit that was not executed");
            return;
        }

        if (db != null) {
            db.saveFiles();
            System.out.println("Deposit recorded: " + getId());
        } else {
            System.err.println("Database not available for recording deposit");
        }
    }

    /**
     * Cancels the operation if any errors occur during the deposit.
     */
    @Override
    public void cancel() {
        System.out.println("Deposit operation cancelled: " + getId());
        // If was executed, reverse the operation
        if (wasExecuted && db != null) {
            BankAccount account = db.readRecord(BankAccount.class, getBankAccountId());
            if (account != null) {
                account.adjustBalance(-getAmount());
                wasExecuted = false;
                System.out.println("Deposit reversed");
            }
        }
    }

    /**
     * Retrieves details about a specific deposit
     */
    @Override
    public void getOperationDetails() {
        System.out.println("=== Deposit Operation Details ===");
        System.out.println("Operation ID: " + getId());
        System.out.println("Bank Account ID: " + getBankAccountId());
        System.out.println("Amount: $" + String.format("%.2f", getAmount()));
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate());
        System.out.println("Status: " + (getIsSuccessfull() ? "Successful" : "Failed"));
        System.out.println("Operation Type: " + getOperationType());
        System.out.println("================================");
    }
}
