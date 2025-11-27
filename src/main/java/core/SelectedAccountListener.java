package core;

import bank.BankAccount;

public interface SelectedAccountListener {
    void onAccountChange(BankAccount selectedAccount);
}
