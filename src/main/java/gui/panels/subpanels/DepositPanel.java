package gui.panels.subpanels;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bank.BankAccount;
import bank.Deposit;
import core.Database;
import core.SelectedAccountListener;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedPanel;
import gui.components.RoundedTextField;

public class DepositPanel extends RoundedPanel implements SelectedAccountListener {
    private Database db;
    private BankAccount selectedAccount;
    private JLabel[] labels;
    private final ImageIcon DEPOSIT_ICON = new ImageIcon(getClass().getResource("/img/deposit-icon.png"));
    private JTextField amountTextField;
    private JTextField descriptionTextField;
    private JButton depositButton;

    public DepositPanel(Database db, BankAccount selectedAccount) {
        super(25);

        this.db = db;
        this.selectedAccount = selectedAccount;

        ThemeManager.styleSecondaryPanel(this);

        labels = new JLabel[4];

        // Title
        labels[0] = new JLabel("Deposit Funds");
        labels[0].setIcon(DEPOSIT_ICON);
        labels[0].setBounds(20, 20, 300, 40);
        ThemeManager.styleProfileTitle(labels[0]);

        // Description
        labels[1] = new JLabel("Add money to your account");
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

        // Deposit Button
        depositButton = new RoundedButton("Deposit");
        depositButton.setBounds(20, 280, 200, 40);
        ThemeManager.styleButton(depositButton);
        addDepositActionListener(depositButton);

        // Add components to the panel
        for (JLabel label : labels) {
            add(label);
        }
        add(amountTextField);
        add(descriptionTextField);
        add(depositButton);
    }

    private void addDepositActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                System.out.println("deposit button");
                String[] depositData = { descriptionTextField.getText(), selectedAccount.getId(),
                        amountTextField.getText(), "2024",
                        "true" };
                Deposit d = db.writeRecord(Deposit.class, depositData);
                selectedAccount.linkOperationId(d.getId());
                selectedAccount.adjustBalance(d.getAmount());
                db.saveFiles();
                JOptionPane.showMessageDialog(this, "Operation Successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void onAccountChange(BankAccount selectedAccount) {
        this.selectedAccount = selectedAccount;
    }
}
