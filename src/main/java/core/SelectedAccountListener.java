package core;

import bank.BankAccount;

public interface SelectedAccountListener {
    void onAccountChange(String selectedAccountId);
}
