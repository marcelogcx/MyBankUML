package gui.panels.subpanels;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bank.BankAccount;
import bank.Client;
import bank.Transfer;
import core.Database;
import core.SelectedAccountListener;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedPanel;
import gui.components.RoundedTextField;

public class TransferPanel extends RoundedPanel implements SelectedAccountListener {
    private Database db;
    private String selectedAccountId;
    private Client client;

    private JLabel[] labels;
    private final ImageIcon TRANSFER_ICON = new ImageIcon(getClass().getResource("/img/transfer-icon.png"));
    private JTextField recipientTextField;
    private JTextField amountTextField;
    private JTextField descriptionTextField;
    private JButton transferButton;

    public TransferPanel(Database db, Client client, String selectedAccountId) {
        super(25);

        this.db = db;
        this.client = client;
        this.selectedAccountId = selectedAccountId;

        ThemeManager.styleSecondaryPanel(this);

        labels = new JLabel[5];

        // Title
        labels[0] = new JLabel("Send Payment");
        labels[0].setIcon(TRANSFER_ICON);
        labels[0].setBounds(20, 20, 300, 40);
        ThemeManager.styleProfileTitle(labels[0]);

        // Description
        labels[1] = new JLabel("Transfer money to another account");
        labels[1].setBounds(20, 60, 400, 35);
        ThemeManager.styleProfileLabel(labels[1]);

        // Recipient Account Label
        labels[2] = new JLabel("Recipient Account ID");
        labels[2].setBounds(20, 95, 200, 40);
        ThemeManager.styleOperationLabel(labels[2]);

        // Recipient Account Text Field
        recipientTextField = new RoundedTextField(false);
        recipientTextField.setBounds(20, 135, 300, 35);

        // Amount Label
        labels[3] = new JLabel("Amount");
        labels[3].setBounds(20, 180, 200, 40);
        ThemeManager.styleOperationLabel(labels[3]);

        // Amount Text Field
        amountTextField = new RoundedTextField(true);
        amountTextField.setBounds(20, 220, 300, 35);

        // Description Label
        labels[4] = new JLabel("Description");
        labels[4].setBounds(20, 265, 200, 40);
        ThemeManager.styleOperationLabel(labels[4]);

        // Description Text Field
        descriptionTextField = new RoundedTextField(false);
        descriptionTextField.setBounds(20, 305, 300, 35);

        // Transfer Button
        transferButton = new RoundedButton("Transfer");
        transferButton.setBounds(20, 365, 200, 40);
        ThemeManager.styleButton(transferButton);
        addTransferActionListener(transferButton);

        // Add components to the panel
        for (JLabel label : labels) {
            add(label);
        }
        add(recipientTextField);
        add(amountTextField);
        add(descriptionTextField);
        add(transferButton);
    }

    public void addTransferActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                Transfer tempWith = this.client.makeTransaction(selectedAccountId, recipientTextField.getText(),
                        Double.parseDouble(amountTextField.getText()),
                        descriptionTextField.getText());
                if (tempWith == null) {
                    JOptionPane.showMessageDialog(null,
                            "Operation failed!\nInsuficient Funds or Recipient ID does not exists", "Failure",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Operation Successful!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    @Override
    public void onAccountChange(String selectedAccountId) {
        this.selectedAccountId = selectedAccountId;
    }
}
