package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankAccount;
import bank.Client;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class DashboardPanel extends JPanel {
    private PanelEventListener listener;
    private JLabel title;
    private JLabel subtitle;
    private ArrayList<JPanel> bankAccountPanels;
    private int numBankAccounts;
    private JButton addBankAccountButton;
    private final ImageIcon ADD_BANK_ACCOUNT_ICON = new ImageIcon(getClass().getResource("/img/add-account-icon.png"));

    public DashboardPanel(Database db, PanelEventListener listener, Client c) {
        this.listener = listener;

        ThemeManager.styleMainPanel(this);

        numBankAccounts = c.getBankAccountIds().size();

        title = new JLabel("Welcome, " + c.getUsername());
        title.setBounds(10, 30, 400, 30);
        ThemeManager.styleH2(title);
        subtitle = new JLabel("Here's an overview of your accounts");
        subtitle.setBounds(10, 60, 400, 40);
        ThemeManager.styleDescription(subtitle);

        ArrayList<BankAccount> tempBankAccounts = db.getClientBankAccounts(c.getBankAccountIds());
        bankAccountPanels = new ArrayList<>();
        for (int i = 0; i < tempBankAccounts.size(); i++) {
            final int INDEX = i;
            BankAccountPanel tempPanel = new BankAccountPanel(tempBankAccounts.get(i));
            if (i % 2 == 0) {
                tempPanel.setBounds(10, 120 + (322 * (i / 2)), 372, 312);
            } else {
                tempPanel.setBounds(392, 120 + (322 * (i / 2)), 372, 312);
            }
            tempPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    AccountTransactionHistoryPanel thp = new AccountTransactionHistoryPanel(db,
                            tempBankAccounts.get(INDEX));

                    listener.onEvent("transactionHistory", thp, new Dimension(0, 0));
                }
            });
            bankAccountPanels.add(tempPanel);
        }

        for (JPanel bankAccountPanel : bankAccountPanels) {
            add(bankAccountPanel);
        }

        addBankAccountButton = new RoundedButton("Add New Account", ADD_BANK_ACCOUNT_ICON);
        addBankAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        addBankAccountButton.setBounds(534, 30, 220, 50);
        addBankAccountButton.setBackground(Color.WHITE);
        addBankAccountButton.addActionListener((ActionEvent e) -> {
            if (e.getSource() == addBankAccountButton) {
                AddNewAccountPanel newAccountPanel = new AddNewAccountPanel(db, listener, c);
                listener.onEvent("addBankAccount", newAccountPanel, new Dimension(0, 0));
            }
        });

        add(title);
        add(subtitle);
        add(addBankAccountButton);
    }

    public int getNumBankAccounts() {
        return numBankAccounts;
    }
}
