package bank;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.BankAccountListener;

import core.Database;

public class BankAccount {
    private List<BankAccountListener> listeners = new ArrayList<>();
    private String id;
    private String accountName;
    private BankAccountType bankAccountType;
    private double balance;
    private ArrayList<String> operationIds;

    public BankAccount(@JsonProperty("id") String id, @JsonProperty("accountName") String accountName,
            @JsonProperty("accountType") BankAccountType bankAccountType, @JsonProperty("balance") double balance,
            @JsonProperty("operationIds") ArrayList<String> operationIds) {
        this.id = id;
        this.accountName = accountName;
        this.bankAccountType = bankAccountType;
        this.balance = balance;
        this.operationIds = operationIds;
    }

    public void addListener(BankAccountListener bal) {
        listeners.add(bal);
    }

    public void removeListener(BankAccountListener bal) {
        listeners.remove(bal);
    }

    public void linkOperationId(String operationId) {
        operationIds.add(operationId);
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountType(BankAccountType accountType) {
        this.bankAccountType = accountType;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public BankAccountType getBankAccountType() {
        return bankAccountType;
    }

    public String getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public double getBalance() {
        return balance;
    }

    public ArrayList<String> getOperationIds() {
        return operationIds;
    }

    /**
     * Adjusts the balance by adding the specified amount (can be positive or
     * negative)
     * 
     * @param amount Amount to add to balance (negative for deductions)
     */
    public void adjustBalance(double amount) {
        balance += amount;
        for (BankAccountListener bal : listeners) {
            bal.onOperation(balance);
        }
    }

    /**
     * Makes a transaction (transfer) to another account
     * Verifies balance then makes transaction
     * 
     * @param recipientAccountId Destination account ID
     * @param amount             Amount to transfer
     * @param description        Description of the transaction
     * @return Transfer object if successful, null otherwise
     */
    public Transfer makeTransaction(Database db, String recipientAccountId, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized for transaction");
            return null;
        }

        String[] recordData = { description, this.id, recipientAccountId,
                String.valueOf(amount),
                java.time.LocalDateTime.now().toString(),
                "false" };
        Transfer transfer = db.writeRecord(Transfer.class, recordData);

        if (transfer != null) {
            transfer.setDatabase(db);
            if (transfer.isValidOperation()) {
                transfer.executeOperation();
                transfer.record();
                return transfer;
            } else {
                transfer.cancel();
            }
        }
        return null;
    }

    /**
     * Makes a withdrawal from the account
     * Processes withdrawal after validation
     * 
     * @param amount      Amount to withdraw
     * @param description Description of the withdrawal
     * @return Withdrawal object if successful, null otherwise
     */
    public Withdrawal makeWithdrawal(Database db, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized for withdrawal");
            return null;
        }

        String[] recordData = { description, this.id,
                String.valueOf(amount),
                java.time.LocalDateTime.now().toString(),
                "false" };
        Withdrawal withdrawal = db.writeRecord(Withdrawal.class, recordData);

        if (withdrawal != null) {
            withdrawal.setDatabase(db);
            if (withdrawal.isValidOperation()) {
                withdrawal.executeOperation();
                withdrawal.record();
                return withdrawal;
            } else {
                withdrawal.cancel();
            }
        }
        return null;
    }

    /**
     * Makes a deposit to the account
     * Processes deposit and adds funds
     * 
     * @param amount      Amount to deposit
     * @param description Description of the deposit
     * @return Deposit object if successful, null otherwise
     */
    public Deposit makeDeposit(Database db, double amount, String description) {
        if (db == null) {
            System.err.println("Database not initialized for deposit");
            return null;
        }

        String[] recordData = { description, this.id,
                String.valueOf(amount),
                java.time.LocalDateTime.now().toString(),
                "false" };
        Deposit deposit = db.writeRecord(Deposit.class, recordData);

        if (deposit != null) {
            deposit.setDatabase(db);
            if (deposit.isValidOperation()) {
                deposit.executeOperation();
                deposit.record();
                return deposit;
            } else {
                deposit.cancel();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return bankAccountType + " - " + id;
    }
}
