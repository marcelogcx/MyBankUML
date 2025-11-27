package core;

import java.util.List;

public interface ClientListener {
    default void onAdditionBankAccount(List<String> bankAccountIds) {
    };
}
