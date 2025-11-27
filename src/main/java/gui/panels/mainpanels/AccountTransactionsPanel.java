package gui.panels.mainpanels;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankAccount;
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

    private TransactionBarPanel transactionBarPanel;

    private JPanel currentPanel;
    private PanelManager pManager;

    public AccountTransactionsPanel(Database db, BankAccount selectedAccount) {
        ThemeManager.styleMainPanel(this);
        listeners = new ArrayList<>();

        // Title and Description
        titleDescriptionPanel = new TitleDescriptionPanel("Transactions",
                "Manage deposits, withdrawals, and transfers");
        titleDescriptionPanel.setBounds(10, 10, 500, 80);

        balancePanel = new CurrentBalancePanel(selectedAccount);
        balancePanel.setBounds(10, 100, 744, 200);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(null, currentPanel);
        currentPanel.setBounds(10, 360, 744, 350);
        currentPanel.setOpaque(false);
        DepositPanel dp = new DepositPanel(db, selectedAccount);
        WithdrawPanel wp = new WithdrawPanel(db, selectedAccount);
        TransferPanel tp = new TransferPanel(db, selectedAccount);
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
