package bank;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankAccount {
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

    public void linkOperationId(String operationId) {
        operationIds.add(operationId);
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

    public void setBalance(double amount) {
        balance += amount;
    }

    @Override
    public String toString() {
        return bankAccountType + " - " + id;
    }
}
