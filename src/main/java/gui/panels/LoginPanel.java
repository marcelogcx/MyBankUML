package gui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bank.Admin;
import bank.Client;
import bank.Teller;
import bank.UserType;
import core.*;
import gui.components.RoundedButton;
import gui.components.RoundedTextField;

public class LoginPanel extends JPanel {

    private PanelEventListener listener;
    private Database db;

    private JLabel[] labels;
    private final ImageIcon LOGO = new ImageIcon(getClass().getResource("/img/logo.png"));
    private RoundedTextField userTextField;
    private RoundedTextField passwordTextField;
    private JButton loginButton;

    public LoginPanel(Database db, PanelEventListener listener) {

        this.listener = listener;
        this.db = db;

        ThemeManager.styleMainPanel(this);

        labels = new JLabel[5];

        // Title
        labels[0] = new JLabel("MyBankUML");
        labels[0].setBounds(0, 0, 500, 100);
        labels[0].setIcon(LOGO);
        labels[0].setHorizontalAlignment(SwingConstants.CENTER);
        ThemeManager.styleTitle(labels[0]);

        // Username Label
        labels[1] = new JLabel("Username:");
        labels[1].setBounds(40, 100, 300, 40);
        ThemeManager.styleFieldLabel(labels[1]);

        // Username TextField
        userTextField = new RoundedTextField(false);
        userTextField.setBounds(40, 140, 420, 40);
        userTextField.setPlaceholder("Enter your username");
        ThemeManager.styleRoundedTextField(userTextField);

        // Password Label
        labels[2] = new JLabel("Password:");
        labels[2].setBounds(40, 200, 300, 40);
        ThemeManager.styleFieldLabel(labels[2]);

        // Password TextField
        passwordTextField = new RoundedTextField(false);
        passwordTextField.setBounds(40, 240, 420, 40);
        passwordTextField.setPlaceholder("Enter your password");
        ThemeManager.styleRoundedTextField(passwordTextField);

        loginButton = new RoundedButton("Log In");
        addLoginActionListener(loginButton);
        loginButton.setBounds((500 - 300) / 2, 330, 300, 40);
        ThemeManager.styleButton(loginButton);

        // New Here Label
        labels[3] = new JLabel("New Here?");
        labels[3].setBounds((500 - 85) / 2 - 60, 390, 85, 20);
        ThemeManager.styleFieldHyperlinkLabel(labels[3]);

        // Create Account Label
        labels[4] = new JLabel("Create an Account");
        labels[4].setBounds((500 - 145) / 2 + 60, 390, 145, 20);
        ThemeManager.styleHyperlink(labels[4]);
        labels[4].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addRegistrationMouseListener(labels[4]); // Make the cursor to change (make it clear is clickable)

        // Add components to the panel
        for (JLabel label : labels) {
            add(label);
        }
        add(userTextField);
        add(passwordTextField);
        add(loginButton);
    }

    private void addLoginActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            String username = "";
            String password = "";
            if (e.getSource() == button) {
                username = userTextField.getText();
                password = passwordTextField.getText();
            }
            if (!db.usernameExists(username)) {
                System.out.println("User does not exist");
                return;
            }
            String userId = db.getIdFromUsername(username);
            Client c = db.readRecord(Client.class, userId);
            if (!c.getPassword().equals(password)) {
                System.out.println("Wrong password");
                return;
            }

            // Route to appropriate panel based on user type
            UserType userType = c.getUserType();

            if (userType == UserType.ADMIN) {
                Admin admin = db.getAdmin(userId);
                if (admin != null) {
                    System.out.println("Admin login successful: " + username);
                    AdminPanel ap = new AdminPanel(db, listener, admin);
                    listener.onEvent("admin", ap, new Dimension(1024, 768));
                }
            } else if (userType == UserType.TELLER) {
                Teller teller = db.getTeller(userId);
                if (teller != null) {
                    System.out.println("Teller login successful: " + username);
                    TellerPanel tp = new TellerPanel(db, listener, teller);
                    listener.onEvent("teller", tp, new Dimension(1024, 768));
                }
            } else {
                // Default to client panel
                System.out.println("Client login successful: " + username);
                MainPanel mp = new MainPanel(db, listener, c);
                listener.onEvent("main", mp, new Dimension(1024, 768));
            }
        });

    }

    private void addRegistrationMouseListener(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegistrationPanel rp = new RegistrationPanel(db, listener);
                listener.onEvent("registration", rp, new Dimension(750, 500));
                resetTextFields();
            }
        });
    }

    public void resetTextFields() {
        userTextField.setText("");
        passwordTextField.setText("");
    }
}
