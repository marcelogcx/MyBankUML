package core;

import java.util.List;

public interface ClientListener {
    void onAdditionBankAccount(List<String> bankAccountIds, String id);
}
