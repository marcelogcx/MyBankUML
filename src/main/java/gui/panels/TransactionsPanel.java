package gui.panels;

import java.awt.CardLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankAccount;
import bank.Client;
import core.Database;
import core.PanelManager;
import core.ThemeManager;
import gui.components.RoundedComboBox;

public class TransactionsPanel extends JPanel {
    private JLabel[] labels;
    private RoundedComboBox<BankAccount> selectAccountBox;
    private CurrentBalancePanel balancePanel;

    private TransactionBarPanel transactionBarPanel;

    private JPanel currentPanel;
    private PanelManager pManager;

    private BankAccount selectedAccount;

    TransactionsPanel(Database db, Client c) {
        ThemeManager.styleMainPanel(this);

        labels = new JLabel[2];

        // Title
        labels[0] = new JLabel("Transactions");
        labels[0].setBounds(10, 10, 300, 40);
        ThemeManager.styleH2(labels[0]);

        // Description
        labels[1] = new JLabel("Manage deposits, withdrawals, and transfers");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        ArrayList<BankAccount> bankAccounts = db.getClientBankAccounts(c.getBankAccountIds());
        selectedAccount = bankAccounts.get(0);
        balancePanel = new CurrentBalancePanel(selectedAccount);
        balancePanel.setBounds(10, 150, 744, 200);

        // Select Account Combo Box

        selectAccountBox = new RoundedComboBox<>();
        for (BankAccount b : bankAccounts) {
            selectAccountBox.addItem(b);
        }
        selectAccountBox.setBounds(10, 100, 345, 40);
        ThemeManager.styleRoundedComboBox(selectAccountBox);

        selectAccountBox.addActionListener(e -> {
            this.selectedAccount = (BankAccount) selectAccountBox.getSelectedItem();
            balancePanel.setSelectedAccount(selectedAccount);
        });

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(null, currentPanel);
        currentPanel.setBounds(10, 410, 744, 350);
        currentPanel.setOpaque(false);
        pManager.addPanel("deposit", new DepositPanel(db, selectedAccount));

        pManager.showPanel("deposit");

        transactionBarPanel = new TransactionBarPanel(pManager, db, selectedAccount);
        transactionBarPanel.setBounds(10, 360, 744, 40);

        add(labels[0]);
        add(labels[1]);
        add(selectAccountBox);
        add(balancePanel);
        add(transactionBarPanel);
        add(currentPanel);

    }

}
