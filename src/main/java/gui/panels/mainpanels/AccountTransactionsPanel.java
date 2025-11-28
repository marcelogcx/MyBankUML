package gui.panels.mainpanels;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bank.BankAccount;
import bank.Client;
import core.ClientListener;
import core.Database;
import core.PanelManager;
import core.ThemeManager;
import gui.panels.subpanels.CurrentBalancePanel;
import gui.panels.subpanels.DepositPanel;
import gui.panels.subpanels.TitleDescriptionPanel;
import gui.panels.subpanels.TransactionBarPanel;
import gui.panels.subpanels.TransferPanel;
import gui.panels.subpanels.WithdrawPanel;
import core.SelectedAccountListener;

public class AccountTransactionsPanel extends JPanel {
    private List<SelectedAccountListener> listeners;
    private JPanel titleDescriptionPanel;
    private CurrentBalancePanel balancePanel;
    private Client client;
    private Database db;
    private String selectedAccountId;

    private TransactionBarPanel transactionBarPanel;

    private JPanel currentPanel;
    private PanelManager pManager;

    public AccountTransactionsPanel(Database db, Client client, String selectedAccountId) {
        ThemeManager.styleMainPanel(this);
        listeners = new ArrayList<>();
        this.db = db;
        this.client = client;
        this.selectedAccountId = selectedAccountId;

        // Title and Description
        titleDescriptionPanel = new TitleDescriptionPanel("Transactions",
                "Manage deposits, withdrawals, and transfers");
        titleDescriptionPanel.setBounds(10, 10, 500, 80);

        List<BankAccount> bankAccounts = db.getClientBankAccounts(client.getBankAccountIds());
        if (bankAccounts.size() == 0) {
            JOptionPane.showMessageDialog(null, "You don't have any accounts!\nCreate one first and try again",
                    "Failure",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        BankAccount selectedAccount = db.readRecord(BankAccount.class, selectedAccountId);
        balancePanel = new CurrentBalancePanel(selectedAccount);
        balancePanel.setBounds(10, 100, 744, 200);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(null, currentPanel);
        currentPanel.setBounds(10, 360, 744, 350);
        currentPanel.setOpaque(false);
        DepositPanel dp = new DepositPanel(db, client, selectedAccountId);
        WithdrawPanel wp = new WithdrawPanel(db, client, selectedAccountId);
        TransferPanel tp = new TransferPanel(db, client, selectedAccountId);
        pManager.addPanel("deposit", dp);
        pManager.addPanel("withdraw", wp);
        pManager.addPanel("transfer", tp);
        addListener(dp);
        addListener(wp);
        addListener(tp);

        pManager.showPanel("deposit");

        transactionBarPanel = new TransactionBarPanel(pManager, db, selectedAccount, currentPanel);
        transactionBarPanel.setBounds(10, 310, 744, 40);

        add(titleDescriptionPanel);
        add(balancePanel);
        add(transactionBarPanel);
        add(currentPanel);
    }

    public void addListener(SelectedAccountListener sal) {
        listeners.add(sal);
    }

    public void removeListener(SelectedAccountListener sal) {
        listeners.remove(sal);
    }

}
