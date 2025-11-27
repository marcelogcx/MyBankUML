package gui.panels.mainpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.xml.crypto.Data;

import bank.BankAccount;
import bank.Client;
import core.ClientListener;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.ModernScrollBarUI;
import gui.components.RoundedButton;
import gui.panels.subpanels.BankAccountPanel;
import gui.panels.subpanels.TitleDescriptionPanel;

public class ClientDashboardPanel extends JPanel implements ClientListener {
    private PanelEventListener listener;
    private Database db;
    private Client c;

    private TitleDescriptionPanel titleDescriptionPanel;
    private ArrayList<JPanel> bankAccountPanels;
    private int numBankAccounts;
    private JButton addBankAccountButton;
    private final ImageIcon ADD_BANK_ACCOUNT_ICON = new ImageIcon(getClass().getResource("/img/add-account-icon.png"));

    public ClientDashboardPanel(Database db, PanelEventListener listener, Client c) {
        this.listener = listener;
        this.db = db;
        this.c = c;

        ThemeManager.styleMainPanel(this);

        numBankAccounts = c.getBankAccountIds().size();

        // Title and Description
        titleDescriptionPanel = new TitleDescriptionPanel("Welcome, " + c.getUsername(),
                "Here's an overview of your accounts");
        titleDescriptionPanel.setBounds(10, 30, 500, 80);

        List<String> bankAccountIds = c.getBankAccountIds();
        createBankAccountPanels(bankAccountIds);

        addBankAccountButton = new RoundedButton("Add New Account", ADD_BANK_ACCOUNT_ICON);
        addBankAccountButton.setFont(new Font("Arial", Font.BOLD, 16));
        addBankAccountButton.setBounds(534, 30, 220, 50);
        addBankAccountButton.setBackground(Color.WHITE);
        addBankAccountButton.addActionListener((ActionEvent e) -> {
            if (e.getSource() == addBankAccountButton) {
                if (c.getIsUserBlocked() == true) {
                    JOptionPane.showMessageDialog(this, "User is blocked\nYou cannot create a bank account", "Failure",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                AddNewAccountPanel newAccountPanel = new AddNewAccountPanel(db, listener, c);
                listener.onAddEvent("addBankAccount", newAccountPanel, new Dimension(0, 0));
            }
        });

        add(titleDescriptionPanel);
        add(addBankAccountButton);
    }

    public int getNumBankAccounts() {
        return numBankAccounts;
    }

    public void setTitleDescription(String titleText, String descriptionText) {
        titleDescriptionPanel.setTitleDescription(titleText, descriptionText);
    }

    public void createBankAccountPanels(List<String> bankAccountIds) {
        List<BankAccount> tempBankAccounts = db.getClientBankAccounts(bankAccountIds);
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
                    AccountTransactionHistoryPanel thp = new AccountTransactionHistoryPanel(db, listener, c,
                            tempBankAccounts.get(INDEX));
                    JScrollPane sp = new JScrollPane(thp);
                    sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                    sp.getVerticalScrollBar().setUnitIncrement(20);
                    sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    sp.setBorder(BorderFactory.createEmptyBorder());
                    AccountTransactionHistoryPanel athp = (AccountTransactionHistoryPanel) thp;
                    int numBankOperations = athp.getNumOperations();
                    if (numBankOperations < 4) {
                        athp.setPreferredSize(new Dimension(774, 650));
                    } else {
                        athp.setPreferredSize(new Dimension(774, 800 + 322 * ((numBankOperations / 2) - 2)));
                    }
                    listener.onAddEvent("transactionHistory", sp, new Dimension(0, 0));
                }
            });
            bankAccountPanels.add(tempPanel);
        }

        for (JPanel bankAccountPanel : bankAccountPanels) {
            add(bankAccountPanel);
        }
    }

    @Override
    public void onAdditionBankAccount(List<String> bankAccountIds) {
        createBankAccountPanels(bankAccountIds);
        int numBankAccounts = bankAccountIds.size();
        if (numBankAccounts < 3) {
            setPreferredSize(new Dimension(774, 650));
        } else {
            setPreferredSize(new Dimension(774, 800 + 322 * ((int) Math.ceil(numBankAccounts / 2.f) - 2)));
        }
        repaint();
    }

}
