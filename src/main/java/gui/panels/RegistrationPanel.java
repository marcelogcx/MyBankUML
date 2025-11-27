package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.BankAccount;
import bank.BankAccountType;
import bank.Client;
import bank.Deposit;
import bank.UserType;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedComboBox;
import gui.components.RoundedTextField;

public class RegistrationPanel extends JPanel {
    private Database db;
    private PanelEventListener listener;

    private JLabel[] labels;
    private RoundedTextField[] textFields;
    private JButton backButton;
    private final ImageIcon BACK_ICON = new ImageIcon(getClass().getResource("/img/back.png"));
    private final ImageIcon REGISTRATION_ICON = new ImageIcon(getClass().getResource("/img/registration-icon.png"));
    private RoundedComboBox<String> accountTypeBox;
    private JButton joinNowButton;

    public RegistrationPanel(Database db, PanelEventListener listener) {
        this.db = db;
        this.listener = listener;

        ThemeManager.styleMainPanel(this);

        labels = new JLabel[7];
        textFields = new RoundedTextField[5];

        // Back Button
        backButton = new RoundedButton("");
        backButton.setIcon(BACK_ICON);
        addBackActionListener(backButton);
        backButton.setBounds(20, 30, 40, 40);

        // Title
        labels[0] = new JLabel("Create New Account");
        labels[0].setIcon(REGISTRATION_ICON);
        labels[0].setBounds((750 - 450) / 2, 0, 450, 100);
        labels[0].setHorizontalAlignment(SwingConstants.CENTER);
        ThemeManager.styleTitle(labels[0]);

        // Full Name Label
        labels[1] = new JLabel("Full Name");
        labels[1].setBounds(20, 100, 100, 50);
        ThemeManager.styleFieldLabel(labels[1]);

        // Full Name Text Field
        textFields[0] = new RoundedTextField(false);
        textFields[0].setBounds(20, 140, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[0]);

        // Email Label
        labels[2] = new JLabel("Email");
        labels[2].setBounds(385, 100, 100, 50);
        ThemeManager.styleFieldLabel(labels[2]);

        // Email Text Field
        textFields[1] = new RoundedTextField(false);
        textFields[1].setBounds(385, 140, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[1]);

        // Username Label
        labels[3] = new JLabel("Username");
        labels[3].setBounds(20, 200, 100, 50);
        ThemeManager.styleFieldLabel(labels[3]);

        // Username Text Field
        textFields[2] = new RoundedTextField(false);
        textFields[2].setBounds(20, 240, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[2]);

        // User Type Label
        labels[4] = new JLabel("User Type");
        labels[4].setBounds(385, 200, 200, 50);
        ThemeManager.styleFieldLabel(labels[4]);

        // User Type Combo Box
        String[] userTypeValues = { "CLIENT", "TELLER", "ADMIN" };
        accountTypeBox = new RoundedComboBox<>(userTypeValues);
        accountTypeBox.setBounds(385, 240, 345, 40);
        ThemeManager.styleRoundedComboBox(accountTypeBox);

        // Password Label
        labels[5] = new JLabel("Password");
        labels[5].setBounds(20, 300, 100, 50);
        ThemeManager.styleFieldLabel(labels[5]);

        // Password Text Field
        textFields[3] = new RoundedTextField(false);
        textFields[3].setBounds(20, 340, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[3]);

        // Initial Deposit Label
        labels[6] = new JLabel("Initial Deposit (Optional)");
        labels[6].setBounds(385, 300, 200, 50);
        ThemeManager.styleFieldLabel(labels[6]);

        // Initial Deposit Text Field
        textFields[4] = new RoundedTextField(true);
        textFields[4].setBounds(385, 340, 345, 40);
        ThemeManager.styleRoundedTextField(textFields[4]);

        joinNowButton = new RoundedButton("Join Now");
        addJoinNowActionListener(joinNowButton);
        joinNowButton.setBounds((750 - 400) / 2, 405, 400, 40);
        ThemeManager.styleButton(joinNowButton);

        // Add components to the panel
        for (JLabel label : labels) {
            add(label);
        }
        for (RoundedTextField tf : textFields) {
            add(tf);
        }
        add(backButton);
        add(accountTypeBox);
        add(joinNowButton);
    }

    private void addBackActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                System.out.println("Back Button");
                LoginPanel loginPanel = new LoginPanel(db, listener);
                listener.onEvent("login", loginPanel, new Dimension(500, 500));
            }
        });
    }

    private void addJoinNowActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            String[] clientData = new String[4];
            String[] accountData = new String[3];
            if (e.getSource() != button) {
                return;
            }
            clientData[0] = textFields[0].getText();
            clientData[1] = textFields[1].getText();
            clientData[2] = textFields[2].getText();
            clientData[3] = textFields[3].getText();

            // Get selected user type
            String selectedUserType = (String) accountTypeBox.getSelectedItem();
            UserType userType = UserType.valueOf(selectedUserType);

            // Create user with specified type
            Client c = db.writeUser(userType, clientData);

            // Create bank account (default to CHECKING type)
            accountData[0] = "Primary Account";
            accountData[1] = "CHECKING";  // BankAccountType
            accountData[2] = textFields[4].getText();
            BankAccount ba = db.writeRecord(BankAccount.class, accountData);

            // Create initial deposit
            String[] operationData = { "Initial Deposit", ba.getId(), accountData[2], "2024", "true" };
            Deposit initialDeposit = db.writeRecord(Deposit.class, operationData);

            c.linkBankAccount(ba.getId());
            ba.linkOperationId(initialDeposit.getId());
            db.saveFiles();

            // Route to appropriate panel based on user type
            if (userType == UserType.ADMIN) {
                AdminPanel ap = new AdminPanel(db, listener, db.getAdmin(c.getId()));
                listener.onEvent("admin", ap, new Dimension(1024, 768));
            } else if (userType == UserType.TELLER) {
                TellerPanel tp = new TellerPanel(db, listener, db.getTeller(c.getId()));
                listener.onEvent("teller", tp, new Dimension(1024, 768));
            } else {
                MainPanel mp = new MainPanel(db, listener, c);
                listener.onEvent("main", mp, new Dimension(1024, 768));
            }
        });
    }

}
