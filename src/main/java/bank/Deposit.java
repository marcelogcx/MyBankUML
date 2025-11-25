package bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Deposit extends BankOperation {

    @JsonCreator
    public Deposit(@JsonProperty("id") String id, @JsonProperty("description") String description,
            @JsonProperty("bankAccountId") String bankAccountId,
            @JsonProperty("amount") double amount, @JsonProperty("date") String date,
            @JsonProperty("isSuccessfull") boolean isSuccessfull) {
        super(id, description, bankAccountId, amount, date, OperationType.DEPOSIT,
                isSuccessfull);
    }

    @JsonIgnore
    @Override
    public boolean isValidOperation() {

        return true;
    }

    @Override
    public void executeOperation() {
    };

    @Override
    public void record() {
    };

    @Override
    public void cancel() {
    };

    @Override
    public void getOperationDetails() {
    };
}
