package core;

import java.util.List;

import bank.User;

public interface ClientListener {
    default void onAdditionBankAccount(List<String> bankAccountIds, String id) {
    };
}
