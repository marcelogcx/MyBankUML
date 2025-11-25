package gui.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.BankAccount;
import bank.Client;
import bank.Deposit;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedComboBox;
import gui.components.RoundedTextField;

public class AddNewAccountPanel extends JPanel {
    private Database db;
    private PanelEventListener listener;
    private Client c;

    private JLabel[] labels;
    private RoundedTextField[] textFields;
    private RoundedComboBox<String> accountTypeBox;
    private JButton addNewBankAccountButton;

    public AddNewAccountPanel(Database db, PanelEventListener listener, Client c) {
        this.db = db;
        this.listener = listener;
        this.c = c;

        ThemeManager.styleMainPanel(this);

        labels = new JLabel[5];
        textFields = new RoundedTextField[2];

        // Title
        labels[0] = new JLabel("Add New Bank Account");
        labels[0].setBounds(10, 10, 400, 40);
        ThemeManager.styleH2(labels[0]);

        // Description
        labels[1] = new JLabel("Enter the details below to open a new bank account");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        // Account Name Label
        labels[2] = new JLabel("Account Name");
        labels[2].setBounds(10, 100, 200, 50);
        ThemeManager.styleFieldLabel(labels[2]);

        // Account Name Text Field
        textFields[0] = new RoundedTextField(false);
        textFields[0].setBounds(10, 150, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[0]);

        // Account Type Label
        labels[3] = new JLabel("Account Type");
        labels[3].setBounds(10, 190, 200, 50);
        ThemeManager.styleFieldLabel(labels[3]);

        // Account Type Combo Box
        String[] accountTypeValues = { "CHECKING", "SAVING", "BUSINESS" };
        accountTypeBox = new RoundedComboBox<>(accountTypeValues);
        accountTypeBox.setBounds(10, 240, 345, 40);
        ThemeManager.styleRoundedComboBox(accountTypeBox);

        // Initial Deposit Label
        labels[4] = new JLabel("Initial Deposit (Optional)");
        labels[4].setBounds(10, 280, 200, 50);
        ThemeManager.styleFieldLabel(labels[4]);

        // Initial Deposit Text Field
        textFields[1] = new RoundedTextField(true);
        textFields[1].setBounds(10, 330, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[1]);

        addNewBankAccountButton = new RoundedButton("Add New Bank Account");
        addNewBankAccountActionListener(addNewBankAccountButton);
        addNewBankAccountButton.setBounds(10, 405, 270, 40);
        ThemeManager.styleButton(addNewBankAccountButton);

        for (JLabel l : labels) {
            add(l);
        }
        for (RoundedTextField tf : textFields) {
            add(tf);
        }
        add(accountTypeBox);
        add(addNewBankAccountButton);

    }

    public void addNewBankAccountActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                String[] bankAccountData = new String[3];
                bankAccountData[0] = textFields[0].getText();
                bankAccountData[1] = (String) accountTypeBox.getSelectedItem();
                bankAccountData[2] = textFields[1].getText();
                BankAccount ba = db.writeRecord(BankAccount.class, bankAccountData);
                c.linkBankAccount(ba.getId());
                String[] operationData = { "Initial Deposit", ba.getId(), bankAccountData[2], "2024", "true" };
                Deposit initialDeposit = db.writeRecord(Deposit.class, operationData);
                ba.linkOperationId(initialDeposit.getId());
                db.saveFiles();

                DashboardPanel dp = new DashboardPanel(db, listener, c);
                listener.onEvent("dashboard", dp, new Dimension(0, 0));
            }

        });
    }
}
