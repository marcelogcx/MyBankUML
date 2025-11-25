package bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "operationType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Deposit.class, name = "DEPOSIT"),
        @JsonSubTypes.Type(value = Transfer.class, name = "TRANSFER"),
        @JsonSubTypes.Type(value = Withdrawal.class, name = "WITHDRAWAL")
})
public abstract class BankOperation {
    private String id;
    private String description;
    private String bankAccountId;
    private double amount;
    private String date;
    private OperationType operationType;
    private boolean isSuccessfull;

    public BankOperation(@JsonProperty("id") String id, @JsonProperty("description") String description,
            @JsonProperty("bankAccountId") String bankAccountId, @JsonProperty("amount") double amount,
            @JsonProperty("date") String date,
            @JsonProperty("operationType") OperationType operationType,
            @JsonProperty("isSuccesfull") boolean isSuccessfull) {
        this.id = id;
        this.description = description;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.operationType = operationType;
        this.isSuccessfull = isSuccessfull;
    }

    public String getId() {
        return id;
    };

    public String getDescription() {
        return description;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public String getDate() {
        return date;
    }

    public String getOperationType() {
        return operationType.toString();
    }

    public double getAmount() {
        return amount;
    }

    public boolean getIsSuccessfull() {
        return isSuccessfull;
    }

    public abstract boolean isValidOperation();

    public abstract void executeOperation();

    public abstract void record();

    public abstract void cancel();

    public abstract void getOperationDetails();

    @Override
    public String toString() {
        return id + " " + this.getClass() + " " + operationType + " " + amount;
    }
}
