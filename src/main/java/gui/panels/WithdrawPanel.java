package gui.panels;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import bank.BankAccount;
import bank.Withdrawal;
import core.Database;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedPanel;
import gui.components.RoundedTextField;

public class WithdrawPanel extends RoundedPanel {
    private Database db;
    private BankAccount selectedAccount;

    private JLabel[] labels;
    private final ImageIcon WITHDRAW_ICON = new ImageIcon(getClass().getResource("/img/withdraw-icon.png"));
    private JTextField amountTextField;
    private JTextField descriptionTextField;
    private JButton withdrawButton;

    WithdrawPanel(Database db, BankAccount selectedAccount) {
        super(25);

        this.db = db;
        this.selectedAccount = selectedAccount;

        ThemeManager.styleSecondaryPanel(this);

        labels = new JLabel[4];

        // Title
        labels[0] = new JLabel("Withdraw Funds");
        labels[0].setIcon(WITHDRAW_ICON);
        labels[0].setBounds(20, 20, 300, 40);
        ThemeManager.styleProfileTitle(labels[0]);

        // Description
        labels[1] = new JLabel("Take money from your account");
        labels[1].setBounds(20, 60, 400, 35);
        ThemeManager.styleProfileLabel(labels[1]);

        // Amount Label
        labels[2] = new JLabel("Amount");
        labels[2].setBounds(20, 95, 200, 40);
        ThemeManager.styleOperationLabel(labels[2]);

        // Amount Text Field
        amountTextField = new RoundedTextField(true);
        amountTextField.setBounds(20, 135, 300, 35);

        // Description Label
        labels[3] = new JLabel("Description");
        labels[3].setBounds(20, 180, 200, 40);
        ThemeManager.styleOperationLabel(labels[3]);

        // Description Text Field
        descriptionTextField = new RoundedTextField(false);
        descriptionTextField.setBounds(20, 220, 300, 35);

        // Withdraw Button
        withdrawButton = new RoundedButton("Withdraw");
        withdrawButton.setBounds(20, 280, 200, 40);
        ThemeManager.styleButton(withdrawButton);
        addWithdrawActionListener(withdrawButton);

        // Add components to the panel
        for (JLabel label : labels) {
            add(label);
        }
        add(amountTextField);
        add(descriptionTextField);
        add(withdrawButton);
    }

    private void addWithdrawActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                System.out.println("deposit button");
                String[] depositData = { descriptionTextField.getText(), selectedAccount.getId(),
                        amountTextField.getText(), "2024",
                        "true" };
                Withdrawal d = db.writeRecord(Withdrawal.class, depositData);
                selectedAccount.linkOperationId(d.getId());
                selectedAccount.adjustBalance(-d.getAmount());
                db.saveFiles();
            }
        });
    }

}
