package gui.panels;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankAccount;
import core.Database;
import core.ThemeManager;

public class AccountTransactionHistoryPanel extends JPanel {
    private JLabel[] labels;
    private CurrentBalancePanel currentBalancePanel;
    private JPanel trasactionHistoryPanel;
    private int numOperations;

    public AccountTransactionHistoryPanel(Database db, BankAccount selectedAccount) {

        ThemeManager.styleMainPanel(this);

        labels = new JLabel[2];

        // Title
        labels[0] = new JLabel(selectedAccount.getAccountName());
        labels[0].setBounds(10, 10, 400, 40);
        ThemeManager.styleH2(labels[0]);

        // Description
        labels[1] = new JLabel("Transaction history of the selected bank account");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        // Current Balance Panel
        currentBalancePanel = new CurrentBalancePanel(selectedAccount);
        currentBalancePanel.setBounds(10, 100, 744, 200);

        // Transaction History Panel
        trasactionHistoryPanel = new TransactionHistoryPanel(db, selectedAccount);
        trasactionHistoryPanel.setBounds(10, 310, 744, 300);

        // Add Components
        add(labels[0]);
        add(labels[1]);
        add(currentBalancePanel);
        add(trasactionHistoryPanel);
    }

    public int getNumOperations() {
        return numOperations;
    }
}
