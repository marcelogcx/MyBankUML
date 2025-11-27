package bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;

public class Transfer extends BankOperation {

    private String recipientId;

    @JsonIgnore
    private Database db;
    @JsonIgnore
    private boolean wasExecuted = false;

    @JsonCreator
    public Transfer(@JsonProperty("id") String id, @JsonProperty("description") String description,
            @JsonProperty("bankAccountId") String bankAccountId,
            @JsonProperty("recipientId") String recipientId, @JsonProperty("amount") double amount,
            @JsonProperty("date") String date,
            @JsonProperty("isSuccessfull") boolean isSuccessfull) {
        super(id, description, bankAccountId, amount, date, OperationType.TRANSFER,
                isSuccessfull);
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Sets the database instance for this transfer
     * @param db Database instance
     */
    public void setDatabase(Database db) {
        this.db = db;
    }

    /**
     * Checks if the transaction is valid (both accounts exist and the sender account has sufficient balance to withdraw).
     * Validates that the sender account has sufficient balance, and that both the sender and recipient accounts exist.
     * @return true if valid, false otherwise
     */
    @JsonIgnore
    @Override
    public boolean isValidOperation() {
        if (getAmount() <= 0) {
            System.err.println("Transfer amount must be positive");
            return false;
        }

        if (db == null) {
            System.err.println("Database not initialized for transfer validation");
            return false;
        }

        // Validate sender account exists
        BankAccount senderAccount = db.readRecord(BankAccount.class, getBankAccountId());
        if (senderAccount == null) {
            System.err.println("Sender bank account not found for transfer");
            return false;
        }

        // Validate recipient account exists
        BankAccount recipientAccount = db.readRecord(BankAccount.class, recipientId);
        if (recipientAccount == null) {
            System.err.println("Recipient bank account not found for transfer");
            return false;
        }

        // Check if sender and recipient are the same
        if (getBankAccountId().equals(recipientId)) {
            System.err.println("Cannot transfer to the same account");
            return false;
        }

        // Check if sender has sufficient balance
        if (senderAccount.getBalance() < getAmount()) {
            System.err.println("Insufficient balance for transfer. Available: $" + senderAccount.getBalance() + ", Requested: $" + getAmount());
            return false;
        }

        return true;
    }

    /**
     * Performs the transfer between the two accounts.
     * Deducts the transfer amount from the sender and adds it to the balance of the recipient bank account.
     */
    @Override
    public void executeOperation() {
        if (!isValidOperation()) {
            System.err.println("Cannot execute invalid transfer operation");
            return;
        }

        if (db == null) {
            System.err.println("Database not initialized for transfer execution");
            return;
        }

        BankAccount senderAccount = db.readRecord(BankAccount.class, getBankAccountId());
        BankAccount recipientAccount = db.readRecord(BankAccount.class, recipientId);

        if (senderAccount == null || recipientAccount == null) {
            System.err.println("One or both accounts not found during transfer execution");
            return;
        }

        // Deduct from sender
        senderAccount.adjustBalance(-getAmount());

        // Add to recipient
        recipientAccount.adjustBalance(getAmount());

        // Link operation to both accounts
        senderAccount.linkOperationId(getId());
        recipientAccount.linkOperationId(getId());

        wasExecuted = true;
        System.out.println("Transfer of $" + getAmount() + " from " + getBankAccountId() + " to " + recipientId + " executed successfully");
    }

    /**
     * Records the transfer details in the database file.
     */
    @Override
    public void record() {
        if (!wasExecuted) {
            System.err.println("Cannot record transfer that was not executed");
            return;
        }

        if (db != null) {
            db.saveFiles();
            System.out.println("Transfer recorded: " + getId());
        } else {
            System.err.println("Database not available for recording transfer");
        }
    }

    /**
     * Cancels the transfer if any part of the process fails, such as insufficient funds of the sender.
     */
    @Override
    public void cancel() {
        System.out.println("Transfer operation cancelled: " + getId());
        // If was executed, reverse the operation
        if (wasExecuted && db != null) {
            BankAccount senderAccount = db.readRecord(BankAccount.class, getBankAccountId());
            BankAccount recipientAccount = db.readRecord(BankAccount.class, recipientId);

            if (senderAccount != null && recipientAccount != null) {
                // Reverse the transfer
                senderAccount.adjustBalance(getAmount());
                recipientAccount.adjustBalance(-getAmount());
                wasExecuted = false;
                System.out.println("Transfer reversed");
            }
        }
    }

    /**
     * Retrieves details about a specific transfer
     */
    @Override
    public void getOperationDetails() {
        System.out.println("=== Transfer Operation Details ===");
        System.out.println("Operation ID: " + getId());
        System.out.println("From Account ID: " + getBankAccountId());
        System.out.println("To Account ID: " + getRecipientId());
        System.out.println("Amount: $" + String.format("%.2f", getAmount()));
        System.out.println("Description: " + getDescription());
        System.out.println("Date: " + getDate());
        System.out.println("Status: " + (getIsSuccessfull() ? "Successful" : "Failed"));
        System.out.println("Operation Type: " + getOperationType());
        System.out.println("==================================");
    }
}
