package gui.panels.mainpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bank.BankAccount;
import bank.Client;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.ModernScrollBarUI;
import gui.components.RoundedButton;
import gui.panels.subpanels.CurrentBalancePanel;

public class AccountTransactionHistoryPanel extends JPanel {
    private JLabel[] labels;
    private CurrentBalancePanel currentBalancePanel;
    private JPanel transactionHistoryPanel;
    private int numOperations;
    private JButton makeTransactionButton;
    private final ImageIcon ADD_BANK_ACCOUNT_ICON = new ImageIcon(getClass().getResource("/img/add-account-icon.png"));

    public AccountTransactionHistoryPanel(Database db, PanelEventListener panelEventListener, Client c,
            BankAccount selectedAccount) {

        ThemeManager.styleMainPanel(this);

        labels = new JLabel[2];

        // Title
        labels[0] = new JLabel(selectedAccount.getAccountName());
        labels[0].setBounds(10, 10, 400, 40);
        ThemeManager.styleTitlePanel(labels[0]);

        // Description
        labels[1] = new JLabel("Transaction history of the selected bank account");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        // Current Balance Panel
        currentBalancePanel = new CurrentBalancePanel(selectedAccount);
        currentBalancePanel.setBounds(10, 100, 744, 200);

        // Transaction History Panel
        transactionHistoryPanel = new TransactionHistoryPanel(db, selectedAccount);
        int numOperations = selectedAccount.getOperationIds().size();
        if (numOperations > 4) {
            transactionHistoryPanel.setPreferredSize(new Dimension(744, 300 + numOperations * 40));
        }
        JScrollPane transactionHistoryScroll = new JScrollPane(transactionHistoryPanel);
        transactionHistoryScroll.setBounds(10, 310, 744, 300);
        transactionHistoryScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        transactionHistoryScroll.getVerticalScrollBar().setUnitIncrement(20);
        transactionHistoryScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        transactionHistoryScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        transactionHistoryScroll.setBorder(BorderFactory.createEmptyBorder());

        makeTransactionButton = new RoundedButton("Make Transaction", ADD_BANK_ACCOUNT_ICON);
        makeTransactionButton.setFont(new Font("Arial", Font.BOLD, 16));
        makeTransactionButton.setBounds(534, 30, 220, 50);
        makeTransactionButton.setBackground(Color.WHITE);
        makeTransactionButton.addActionListener((ActionEvent e) -> {
            if (c.getIsUserBlocked() == true) {
                JOptionPane.showMessageDialog(null, "User is blocked\nYou cannot make transactions", "Failure",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (e.getSource() == makeTransactionButton) {
                AccountTransactionsPanel atp = new AccountTransactionsPanel(db, c, selectedAccount.getId());
                selectedAccount.setDatabase(db);
                JScrollPane transactionsScroll = new JScrollPane(atp);
                transactionsScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                transactionsScroll.getVerticalScrollBar().setUnitIncrement(20);
                transactionsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                transactionsScroll.setBorder(BorderFactory.createEmptyBorder());
                atp.setPreferredSize(new Dimension(774, 900));
                panelEventListener.onAddEvent("accountTransaction", transactionsScroll, new Dimension(0, 0));
            }
        });

        // Add Components
        add(labels[0]);
        add(labels[1]);
        add(currentBalancePanel);
        add(transactionHistoryScroll);
        add(makeTransactionButton);
    }

    public int getNumOperations() {
        return numOperations;
    }

}
