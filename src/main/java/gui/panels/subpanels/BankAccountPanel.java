package gui.panels.subpanels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import bank.BankAccount;
import core.ThemeManager;
import gui.components.RoundedLabel;
import gui.components.RoundedPanel;

public class BankAccountPanel extends RoundedPanel {
    private JLabel[] labels;
    private final ImageIcon CHECKING_ICON = new ImageIcon(getClass().getResource("/img/checking-icon.png"));
    private final ImageIcon SAVING_ICON = new ImageIcon(getClass().getResource("/img/saving-icon.png"));
    private final ImageIcon BUSINESS_ICON = new ImageIcon(getClass().getResource("/img/business-icon.png"));

    public BankAccountPanel(BankAccount b) {
        super(25);

        ThemeManager.styleSecondaryPanel(this);

        labels = new JLabel[9];

        // Account Name
        labels[0] = new JLabel(b.getAccountName());
        labels[0].setBounds(10, 20, 300, 40);
        ThemeManager.styleTitleBankAccount(labels[0]);

        // Account Type
        labels[1] = new RoundedLabel(b.getBankAccountType().toString(), 10);
        labels[1].setBounds(262, 20, 100, 25);
        ThemeManager.styleAccountType(labels[1]);

        // Account ID Label
        labels[2] = new JLabel("Account ID");
        labels[2].setBounds(10, 80, 200, 20);

        // Acount ID Value
        labels[3] = new JLabel(b.getId());
        labels[3].setBounds(10, 110, 200, 20);

        ThemeManager.styleAccountId(labels[2], labels[3]);

        // Icon (Depending on the Bank Account Type)
        labels[4] = new JLabel();
        switch (b.getBankAccountType()) {
            case CHECKING -> {
                labels[4].setIcon(CHECKING_ICON);
            }
            case SAVINGS -> {
                labels[4].setIcon(SAVING_ICON);
            }
            case BUSINESS -> {
                labels[4].setIcon(BUSINESS_ICON);
            }
        }
        labels[4].setBounds(10, 160, 64, 64);

        // Current Balance Label
        labels[5] = new JLabel("Current Balance");
        labels[5].setBounds(84, 160, 200, 20);

        // Current Balance Value
        labels[6] = new JLabel("$" + b.getBalance());
        labels[6].setBounds(84, 180, 300, 44);

        ThemeManager.styleCurrentBalance(labels[5], labels[6]);

        // Separator Horizontal Line
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setForeground(new Color(107, 124, 147, 95));
        sep.setBounds(10, 254, 352, 1);
        add(sep);

        // Status Label
        labels[7] = new JLabel("Status");
        labels[7].setBounds(10, 264, 100, 20);

        // Status Value
        labels[8] = new RoundedLabel("ACTIVE", 10);
        labels[8].setBounds(262, 264, 100, 20);
        labels[8].setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ThemeManager.styleStatus(labels[7], labels[8]);

        // Add Components to this panel
        for (JLabel l : labels) {
            add(l);
        }
    }
}
