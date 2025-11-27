package gui.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bank.Client;
import bank.Teller;
import bank.UserType;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class TellerPanel extends JPanel {
    private Database db;
    private PanelEventListener listener;
    private Teller teller;

    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private JLabel infoLabel;
    private JButton viewClientsButton;
    private JButton registerClientButton;
    private JButton makeDepositButton;
    private JButton makeWithdrawalButton;
    private JButton makeTransferButton;
    private JButton logoutButton;

    public TellerPanel(Database db, PanelEventListener listener, Teller teller) {
        this.db = db;
        this.listener = listener;
        this.teller = teller;

        setLayout(null);
        ThemeManager.styleMainPanel(this);

        // Title
        titleLabel = new JLabel("Teller Dashboard");
        titleLabel.setBounds(50, 30, 400, 40);
        ThemeManager.styleH2(titleLabel);

        // Welcome message
        welcomeLabel = new JLabel("Welcome, " + teller.getUsername() + " (Bank Teller)");
        welcomeLabel.setBounds(50, 80, 600, 30);
        ThemeManager.styleProfileLabel(welcomeLabel);

        // Info
        infoLabel = new JLabel("Teller ID: " + teller.getTellerId());
        infoLabel.setBounds(50, 120, 400, 30);
        ThemeManager.styleFieldLabel(infoLabel);

        // View Clients Button
        viewClientsButton = new RoundedButton("View Clients");
        viewClientsButton.setBounds(50, 180, 200, 40);
        ThemeManager.styleButton(viewClientsButton);
        viewClientsButton.addActionListener(e -> {
            showAllClientsDialog();
        });

        // Register Client Button
        registerClientButton = new RoundedButton("Register Client");
        registerClientButton.setBounds(270, 180, 200, 40);
        ThemeManager.styleButton(registerClientButton);
        registerClientButton.addActionListener(e -> {
            showRegisterClientDialog();
        });

        // Make Deposit Button
        makeDepositButton = new RoundedButton("Make Deposit");
        makeDepositButton.setBounds(50, 240, 200, 40);
        ThemeManager.styleButton(makeDepositButton);
        makeDepositButton.addActionListener(e -> {
            showMakeDepositDialog();
        });

        // Make Withdrawal Button
        makeWithdrawalButton = new RoundedButton("Make Withdrawal");
        makeWithdrawalButton.setBounds(270, 240, 200, 40);
        ThemeManager.styleButton(makeWithdrawalButton);
        makeWithdrawalButton.addActionListener(e -> {
            showMakeWithdrawalDialog();
        });

        // Make Transfer Button
        makeTransferButton = new RoundedButton("Make Transfer");
        makeTransferButton.setBounds(50, 300, 200, 40);
        ThemeManager.styleButton(makeTransferButton);
        makeTransferButton.addActionListener(e -> {
            showMakeTransferDialog();
        });

        // Logout Button
        logoutButton = new RoundedButton("Logout");
        logoutButton.setBounds(270, 300, 200, 40);
        ThemeManager.styleButton(logoutButton);
        logoutButton.addActionListener(e -> {
            teller.signout();
            LoginPanel lp = new LoginPanel(db, listener);
            listener.onEvent("login", lp, new Dimension(500, 500));
        });

        // Add components
        add(titleLabel);
        add(welcomeLabel);
        add(infoLabel);
        add(viewClientsButton);
        add(registerClientButton);
        add(makeDepositButton);
        add(makeWithdrawalButton);
        add(makeTransferButton);
        add(logoutButton);
    }

    private void showAllClientsDialog() {
        ArrayList<Client> clients = db.getAllUsers();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "All Clients", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        String[] columnNames = {"Client ID", "Username", "Full Name", "Email", "# Accounts"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Client client : clients) {
            Object[] row = {
                client.getId(),
                client.getUsername(),
                client.getFullname(),
                client.getEmail(),
                client.getBankAccountIds().size()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 750, 350);

        JButton viewDetailsButton = new RoundedButton("View Selected Client");
        viewDetailsButton.setBounds(250, 390, 200, 40);
        ThemeManager.styleButton(viewDetailsButton);
        viewDetailsButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a client first", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String clientId = (String) tableModel.getValueAt(selectedRow, 0);
            Client client = teller.viewClient(clientId);
            if (client != null) {
                String message = String.format(
                    "Client Details:\n\n" +
                    "ID: %s\n" +
                    "Username: %s\n" +
                    "Full Name: %s\n" +
                    "Email: %s\n" +
                    "User Type: %s\n" +
                    "Bank Accounts: %d",
                    client.getId(),
                    client.getUsername(),
                    client.getFullname(),
                    client.getEmail(),
                    client.getUserType(),
                    client.getBankAccountIds().size()
                );
                JOptionPane.showMessageDialog(dialog, message, "Client Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton closeButton = new RoundedButton("Close");
        closeButton.setBounds(470, 390, 100, 40);
        ThemeManager.styleButton(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(scrollPane);
        dialog.add(viewDetailsButton);
        dialog.add(closeButton);
        dialog.setVisible(true);
    }

    private void showRegisterClientDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Register New Client", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel fullnameLabel = new JLabel("Full Name:");
        fullnameLabel.setBounds(50, 30, 150, 25);
        JTextField fullnameField = new JTextField();
        fullnameField.setBounds(200, 30, 250, 30);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 80, 150, 25);
        JTextField emailField = new JTextField();
        emailField.setBounds(200, 80, 250, 30);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 130, 150, 25);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(200, 130, 250, 30);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 180, 150, 25);
        JTextField passwordField = new JTextField();
        passwordField.setBounds(200, 180, 250, 30);

        JButton registerButton = new RoundedButton("Register Client");
        registerButton.setBounds(150, 280, 200, 40);
        ThemeManager.styleButton(registerButton);
        registerButton.addActionListener(e -> {
            String fullname = fullnameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (fullname.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client newClient = teller.register(UserType.CLIENT, fullname, email, username, password);
            if (newClient != null) {
                db.saveFiles();
                JOptionPane.showMessageDialog(dialog,
                    "Client registered successfully!\nClient ID: " + newClient.getId(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Failed to register client. Username may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(fullnameLabel);
        dialog.add(fullnameField);
        dialog.add(emailLabel);
        dialog.add(emailField);
        dialog.add(usernameLabel);
        dialog.add(usernameField);
        dialog.add(passwordLabel);
        dialog.add(passwordField);
        dialog.add(registerButton);
        dialog.setVisible(true);
    }

    private void showMakeDepositDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Make Deposit for Client", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel clientIdLabel = new JLabel("Client ID:");
        clientIdLabel.setBounds(50, 30, 150, 25);
        JTextField clientIdField = new JTextField();
        clientIdField.setBounds(200, 30, 250, 30);

        JLabel accountIdLabel = new JLabel("Account ID:");
        accountIdLabel.setBounds(50, 80, 150, 25);
        JTextField accountIdField = new JTextField();
        accountIdField.setBounds(200, 80, 250, 30);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 130, 150, 25);
        JTextField amountField = new JTextField();
        amountField.setBounds(200, 130, 250, 30);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 180, 150, 25);
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(200, 180, 250, 30);

        JButton depositButton = new RoundedButton("Make Deposit");
        depositButton.setBounds(150, 250, 200, 40);
        ThemeManager.styleButton(depositButton);
        depositButton.addActionListener(e -> {
            try {
                String clientId = clientIdField.getText().trim();
                String accountId = accountIdField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();

                if (clientId.isEmpty() || accountId.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (teller.makeDeposit(clientId, accountId, amount, description) != null) {
                    db.saveFiles();
                    JOptionPane.showMessageDialog(dialog,
                        "Deposit completed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to make deposit. Check the client ID and account ID.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(clientIdLabel);
        dialog.add(clientIdField);
        dialog.add(accountIdLabel);
        dialog.add(accountIdField);
        dialog.add(amountLabel);
        dialog.add(amountField);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(depositButton);
        dialog.setVisible(true);
    }

    private void showMakeWithdrawalDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Make Withdrawal for Client", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel clientIdLabel = new JLabel("Client ID:");
        clientIdLabel.setBounds(50, 30, 150, 25);
        JTextField clientIdField = new JTextField();
        clientIdField.setBounds(200, 30, 250, 30);

        JLabel accountIdLabel = new JLabel("Account ID:");
        accountIdLabel.setBounds(50, 80, 150, 25);
        JTextField accountIdField = new JTextField();
        accountIdField.setBounds(200, 80, 250, 30);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 130, 150, 25);
        JTextField amountField = new JTextField();
        amountField.setBounds(200, 130, 250, 30);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 180, 150, 25);
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(200, 180, 250, 30);

        JButton withdrawButton = new RoundedButton("Make Withdrawal");
        withdrawButton.setBounds(150, 250, 200, 40);
        ThemeManager.styleButton(withdrawButton);
        withdrawButton.addActionListener(e -> {
            try {
                String clientId = clientIdField.getText().trim();
                String accountId = accountIdField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();

                if (clientId.isEmpty() || accountId.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (teller.makeWithdrawal(clientId, accountId, amount, description) != null) {
                    db.saveFiles();
                    JOptionPane.showMessageDialog(dialog,
                        "Withdrawal completed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to make withdrawal. Check the client ID, account ID, and balance.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(clientIdLabel);
        dialog.add(clientIdField);
        dialog.add(accountIdLabel);
        dialog.add(accountIdField);
        dialog.add(amountLabel);
        dialog.add(amountField);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(withdrawButton);
        dialog.setVisible(true);
    }

    private void showMakeTransferDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "Make Transfer for Client", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        JLabel clientIdLabel = new JLabel("Client ID:");
        clientIdLabel.setBounds(50, 30, 150, 25);
        JTextField clientIdField = new JTextField();
        clientIdField.setBounds(200, 30, 250, 30);

        JLabel fromAccountLabel = new JLabel("From Account ID:");
        fromAccountLabel.setBounds(50, 80, 150, 25);
        JTextField fromAccountField = new JTextField();
        fromAccountField.setBounds(200, 80, 250, 30);

        JLabel toAccountLabel = new JLabel("To Account ID:");
        toAccountLabel.setBounds(50, 130, 150, 25);
        JTextField toAccountField = new JTextField();
        toAccountField.setBounds(200, 130, 250, 30);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 180, 150, 25);
        JTextField amountField = new JTextField();
        amountField.setBounds(200, 180, 250, 30);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 230, 150, 25);
        JTextField descriptionField = new JTextField();
        descriptionField.setBounds(200, 230, 250, 30);

        JButton transferButton = new RoundedButton("Make Transfer");
        transferButton.setBounds(150, 300, 200, 40);
        ThemeManager.styleButton(transferButton);
        transferButton.addActionListener(e -> {
            try {
                String clientId = clientIdField.getText().trim();
                String fromAccountId = fromAccountField.getText().trim();
                String toAccountId = toAccountField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String description = descriptionField.getText().trim();

                if (clientId.isEmpty() || fromAccountId.isEmpty() || toAccountId.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (teller.makeTransaction(clientId, fromAccountId, toAccountId, amount, description) != null) {
                    db.saveFiles();
                    JOptionPane.showMessageDialog(dialog,
                        "Transfer completed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                        "Failed to make transfer. Check account IDs and balance.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid amount format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(clientIdLabel);
        dialog.add(clientIdField);
        dialog.add(fromAccountLabel);
        dialog.add(fromAccountField);
        dialog.add(toAccountLabel);
        dialog.add(toAccountField);
        dialog.add(amountLabel);
        dialog.add(amountField);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(transferButton);
        dialog.setVisible(true);
    }
}
