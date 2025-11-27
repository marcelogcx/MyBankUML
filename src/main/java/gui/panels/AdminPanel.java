package gui.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import bank.Admin;
import bank.Client;
import bank.UserType;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class AdminPanel extends JPanel {
    private Database db;
    private PanelEventListener listener;
    private Admin admin;

    private JLabel titleLabel;
    private JLabel welcomeLabel;
    private JLabel infoLabel;
    private JButton viewUsersButton;
    private JButton registerUserButton;
    private JButton logoutButton;

    public AdminPanel(Database db, PanelEventListener listener, Admin admin) {
        this.db = db;
        this.listener = listener;
        this.admin = admin;

        setLayout(null);
        ThemeManager.styleMainPanel(this);

        // Title
        titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setBounds(50, 30, 400, 40);
        ThemeManager.styleH2(titleLabel);

        // Welcome message
        welcomeLabel = new JLabel("Welcome, " + admin.getUsername() + " (Administrator)");
        welcomeLabel.setBounds(50, 80, 600, 30);
        ThemeManager.styleProfileLabel(welcomeLabel);

        // Info
        infoLabel = new JLabel("Admin ID: " + admin.getAdminID());
        infoLabel.setBounds(50, 120, 400, 30);
        ThemeManager.styleFieldLabel(infoLabel);

        // View Users Button
        viewUsersButton = new RoundedButton("View All Users");
        viewUsersButton.setBounds(50, 180, 200, 40);
        ThemeManager.styleButton(viewUsersButton);
        viewUsersButton.addActionListener(e -> {
            showAllUsersDialog();
        });

        // Register User Button
        registerUserButton = new RoundedButton("Register New User");
        registerUserButton.setBounds(270, 180, 200, 40);
        ThemeManager.styleButton(registerUserButton);
        registerUserButton.addActionListener(e -> {
            RegistrationPanel rp = new RegistrationPanel(db, listener);
            listener.onEvent("registration", rp, new Dimension(750, 500));
        });

        // Logout Button
        logoutButton = new RoundedButton("Logout");
        logoutButton.setBounds(50, 240, 200, 40);
        ThemeManager.styleButton(logoutButton);
        logoutButton.addActionListener(e -> {
            admin.signout();
            LoginPanel lp = new LoginPanel(db, listener);
            listener.onEvent("login", lp, new Dimension(500, 500));
        });

        // Add components
        add(titleLabel);
        add(welcomeLabel);
        add(infoLabel);
        add(viewUsersButton);
        add(registerUserButton);
        add(logoutButton);
    }

    private void showAllUsersDialog() {
        ArrayList<Client> users = admin.getAllUsers();

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parentFrame, "All Users", true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);

        // Create table columns
        String[] columnNames = {"User ID", "Username", "Full Name", "Email", "User Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Populate table with user data
        for (Client user : users) {
            Object[] row = {
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getEmail(),
                user.getUserType().toString()
            };
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 750, 350);

        // Grant Access Button
        JButton grantAccessButton = new RoundedButton("Grant Access to Selected User");
        grantAccessButton.setBounds(250, 390, 280, 40);
        ThemeManager.styleButton(grantAccessButton);
        grantAccessButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Please select a user first", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String userId = (String) tableModel.getValueAt(selectedRow, 0);
            String username = (String) tableModel.getValueAt(selectedRow, 1);
            String currentType = (String) tableModel.getValueAt(selectedRow, 4);

            // Show dialog to select new user type
            UserType[] userTypes = UserType.values();
            UserType newType = (UserType) JOptionPane.showInputDialog(
                dialog,
                "Select new user type for " + username + " (current: " + currentType + ")",
                "Grant Access",
                JOptionPane.QUESTION_MESSAGE,
                null,
                userTypes,
                UserType.valueOf(currentType)
            );

            if (newType != null && admin.grantAccess(userId, newType)) {
                tableModel.setValueAt(newType.toString(), selectedRow, 4);
                db.saveFiles();
                JOptionPane.showMessageDialog(dialog,
                    "Successfully updated " + username + " to " + newType,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Close Button
        JButton closeButton = new RoundedButton("Close");
        closeButton.setBounds(550, 390, 100, 40);
        ThemeManager.styleButton(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.add(scrollPane);
        dialog.add(grantAccessButton);
        dialog.add(closeButton);
        dialog.setVisible(true);
    }
}
