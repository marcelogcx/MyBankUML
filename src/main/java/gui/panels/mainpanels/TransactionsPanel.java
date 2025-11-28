package gui.panels.mainpanels;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import bank.BankAccount;
import bank.Client;
import bank.User;
import core.ClientListener;
import core.Database;
import core.PanelManager;
import core.SelectedAccountListener;
import core.ThemeManager;
import gui.components.RoundedComboBox;
import gui.panels.subpanels.CurrentBalancePanel;
import gui.panels.subpanels.DepositPanel;
import gui.panels.subpanels.TransactionBarPanel;
import gui.panels.subpanels.TransferPanel;
import gui.panels.subpanels.WithdrawPanel;

public class TransactionsPanel extends JPanel implements ClientListener {
    private JLabel[] labels;
    private List<SelectedAccountListener> listeners;
    private Database db;
    private Client c;

    private RoundedComboBox<BankAccount> selectAccountBox;
    private CurrentBalancePanel balancePanel;

    private TransactionBarPanel transactionBarPanel;

    private JPanel currentPanel;
    private PanelManager pManager;

    private BankAccount selectedAccount;

    public TransactionsPanel(Database db, Client c) {
        ThemeManager.styleMainPanel(this);

        this.db = db;
        this.c = c;
        c.addClientListener(this);
        c.setDatabase(db);

        listeners = new ArrayList<>();
        labels = new JLabel[2];

        // Title
        labels[0] = new JLabel("Transactions");
        labels[0].setBounds(10, 10, 300, 40);
        ThemeManager.styleTitlePanel(labels[0]);

        // Description
        labels[1] = new JLabel("Manage deposits, withdrawals, and transfers");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        List<BankAccount> bankAccounts = db.getClientBankAccounts(c.getBankAccountIds());
        if (bankAccounts.size() == 0) {
            add(labels[0]);
            add(labels[1]);

            return;
        }
        selectedAccount = bankAccounts.get(0);
        selectedAccount.setDatabase(db);
        System.out.println(selectedAccount.getBalance());
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
            for (SelectedAccountListener sal : listeners) {
                sal.onAccountChange(selectedAccount.getId());
            }
        });

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(null, currentPanel);
        currentPanel.setBounds(10, 410, 744, 350);
        currentPanel.setOpaque(false);
        DepositPanel dp = new DepositPanel(db, c, selectedAccount.getId());
        WithdrawPanel wp = new WithdrawPanel(db, c, selectedAccount.getId());
        TransferPanel tp = new TransferPanel(db, c, selectedAccount.getId());
        pManager.addPanel("deposit", dp);
        pManager.addPanel("withdraw", wp);
        pManager.addPanel("transfer", tp);
        addListener(dp);
        addListener(wp);
        addListener(tp);

        pManager.showPanel("deposit");

        transactionBarPanel = new TransactionBarPanel(pManager, db, selectedAccount, currentPanel);
        transactionBarPanel.setBounds(10, 360, 744, 40);

        add(labels[0]);
        add(labels[1]);
        add(selectAccountBox);
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

    @Override
    public void onAdditionBankAccount(List<String> bankAccountIds, String id) {

        List<BankAccount> bankAccounts = db.getClientBankAccounts(c.getBankAccountIds());
        if (bankAccounts.size() > 1) {
            remove(balancePanel);
            remove(selectAccountBox);
        }
        selectedAccount = bankAccounts.get(0);
        selectedAccount.setDatabase(db);
        balancePanel = new CurrentBalancePanel(selectedAccount);
        balancePanel.setBounds(10, 150, 744, 200);

        selectAccountBox = new RoundedComboBox<>();
        for (BankAccount b : bankAccounts) {
            selectAccountBox.addItem(b);
        }
        selectAccountBox.setBounds(10, 100, 345, 40);
        ThemeManager.styleRoundedComboBox(selectAccountBox);

        selectAccountBox.addActionListener(e -> {
            this.selectedAccount = (BankAccount) selectAccountBox.getSelectedItem();
            balancePanel.setSelectedAccount(selectedAccount);
            for (SelectedAccountListener sal : listeners) {
                sal.onAccountChange(selectedAccount.getId());
            }
        });
        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(null, currentPanel);
        currentPanel.setBounds(10, 410, 744, 350);
        currentPanel.setOpaque(false);
        DepositPanel dp = new DepositPanel(db, c, selectedAccount.getId());
        WithdrawPanel wp = new WithdrawPanel(db, c, selectedAccount.getId());
        TransferPanel tp = new TransferPanel(db, c, selectedAccount.getId());
        pManager.addPanel("deposit", dp);
        pManager.addPanel("withdraw", wp);
        pManager.addPanel("transfer", tp);
        addListener(dp);
        addListener(wp);
        addListener(tp);

        pManager.showPanel("deposit");

        transactionBarPanel = new TransactionBarPanel(pManager, db, selectedAccount, currentPanel);
        transactionBarPanel.setBounds(10, 360, 744, 40);

        add(selectAccountBox);
        add(balancePanel);
        add(transactionBarPanel);
        add(currentPanel);
        revalidate();
        repaint();
    }

}
