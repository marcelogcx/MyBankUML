package gui.panels.mainpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.Admin;
import bank.BankAccount;
import bank.BankAccountType;
import bank.Client;
import bank.Deposit;
import bank.Teller;
import bank.User;
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
    private RoundedComboBox<UserType> userTypeBox;
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
        textFields[0].setPlaceholder("e.g. John Smith");
        ThemeManager.styleRoundedTextField(textFields[0]);

        // Email Label
        labels[2] = new JLabel("Email");
        labels[2].setBounds(385, 100, 100, 50);
        ThemeManager.styleFieldLabel(labels[2]);

        // Email Text Field
        textFields[1] = new RoundedTextField(false);
        textFields[1].setBounds(385, 140, 345, 40);
        textFields[1].setPlaceholder("e.g. asd@hotmail.com");
        ThemeManager.styleRoundedTextField(textFields[1]);

        // Username Label
        labels[3] = new JLabel("Username");
        labels[3].setBounds(20, 200, 100, 50);
        ThemeManager.styleFieldLabel(labels[3]);

        // Username Text Field
        textFields[2] = new RoundedTextField(false);
        textFields[2].setBounds(20, 240, 345, 40);
        textFields[2].setPlaceholder("e.g. john123");
        ThemeManager.styleRoundedTextField(textFields[2]);

        // User Type Label
        labels[4] = new JLabel("User Type");
        labels[4].setBounds(385, 200, 200, 50);
        ThemeManager.styleFieldLabel(labels[4]);

        // User Type Combo Box
        userTypeBox = new RoundedComboBox<>(UserType.values());
        userTypeBox.setBounds(385, 240, 345, 40);
        ThemeManager.styleRoundedComboBox(userTypeBox);

        // Password Label
        labels[5] = new JLabel("Password");
        labels[5].setBounds(20, 300, 100, 50);
        ThemeManager.styleFieldLabel(labels[5]);

        // Password Text Field
        textFields[3] = new RoundedTextField(false);
        textFields[3].setBounds(20, 340, 345, 40);
        textFields[3].setPlaceholder("e.g. 12345");
        ThemeManager.styleRoundedTextField(textFields[3]);

        // Confirm Password Label
        labels[6] = new JLabel("Confirm Password");
        labels[6].setBounds(385, 300, 200, 50);
        ThemeManager.styleFieldLabel(labels[6]);

        // Confirm Password Text Field
        textFields[4] = new RoundedTextField(false);
        textFields[4].setBounds(385, 340, 345, 40);
        textFields[4].setPlaceholder("e.g. 12345");
        ThemeManager.styleRoundedTextField(textFields[4]);

        // Join Button
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
        add(userTypeBox);
        add(joinNowButton);
    }

    private void addBackActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                System.out.println("Back Button");
                LoginPanel loginPanel = new LoginPanel(db, listener);
                listener.onAddEvent("login", loginPanel, new Dimension(500, 500));
            }
        });
    }

    private void addJoinNowActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            String[] userData = new String[6];
            if (e.getSource() != button) {
                return;
            }
            userData[0] = textFields[0].getText();
            userData[1] = textFields[1].getText();
            userData[2] = textFields[2].getText();
            userData[3] = textFields[3].getText();
            UserType userType = (UserType) userTypeBox.getSelectedItem();
            userData[4] = userType.toString();
            userData[5] = textFields[4].getText();

            if (!userData[3].equals(userData[5])) {
                System.out.println("Not same password");
                return;
            }
            switch (userType) {
                case CLIENT:
                    Client c = db.writeRecord(Client.class, userData);
                    db.saveFiles();
                    ClientMainPanel cmp = new ClientMainPanel(db, listener, c);
                    listener.onAddEvent("main", cmp, new Dimension(1024, 768));
                    break;
                case TELLER:
                    Teller t = db.writeRecord(Teller.class, userData);
                    db.saveFiles();
                    TellerMainPanel tmp = new TellerMainPanel(db, listener, t);
                    listener.onAddEvent("main", tmp, new Dimension(1024, 768));
                    break;
                case ADMIN:
                    Admin a = db.writeRecord(Admin.class, userData);
                    db.saveFiles();
                    AdminMainPanel amp = new AdminMainPanel(db, listener, a);
                    listener.onAddEvent("main", amp, new Dimension(1024, 768));
                    break;
            }

        });
    }

}
