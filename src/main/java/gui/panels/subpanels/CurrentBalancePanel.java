package gui.panels.subpanels;

import javax.swing.JLabel;

import bank.BankAccount;
import core.BankAccountListener;
import core.ThemeManager;
import gui.components.RoundedPanel;

public class CurrentBalancePanel extends RoundedPanel
        implements BankAccountListener {
    private JLabel[] labels;
    private BankAccount selectedAccount;

    public CurrentBalancePanel(BankAccount selectedAccount) {
        super(25);

        this.selectedAccount = selectedAccount;
        selectedAccount.addListener(this);

        ThemeManager.styleSecondaryPanel(this);

        labels = new JLabel[3];

        // Current Balance Title
        labels[0] = new JLabel("Current Balance");
        labels[0].setBounds(20, 20, 300, 40);
        ThemeManager.styleProfileTitle(labels[0]);

        // Account ID
        labels[1] = new JLabel(
                this.selectedAccount.getBankAccountType() + " ACCOUNT" + " - " + this.selectedAccount.getId());
        labels[1].setBounds(20, 60, 400, 40);
        ThemeManager.styleProfileLabel(labels[1]);

        // Current Balance Value
        labels[2] = new JLabel("$" + this.selectedAccount.getBalance());
        labels[2].setBounds(20, 120, 400, 40);
        ThemeManager.styleBalance(labels[2]);

        add(labels[0]);
        add(labels[1]);
        add(labels[2]);
    }

    public void setSelectedAccount(BankAccount selectedAccount) {
        this.selectedAccount.removeListener(this);
        this.selectedAccount = selectedAccount;
        this.selectedAccount.addListener(this);
        labels[1]
                .setText(this.selectedAccount.getBankAccountType() + " ACCOUNT" + " - " + this.selectedAccount.getId());
        labels[2].setText("$" + this.selectedAccount.getBalance());
        revalidate();
        repaint();
    }

    @Override
    public void onOperation(double balance) {
        labels[2].setText("$" + balance);
        revalidate();
        repaint();
    }
}
