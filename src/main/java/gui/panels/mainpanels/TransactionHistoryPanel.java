package gui.panels.mainpanels;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import bank.BankAccount;
import bank.BankOperation;
import core.Database;
import core.ThemeManager;
import gui.components.RoundedPanel;

public class TransactionHistoryPanel extends RoundedPanel {
    private ArrayList<JLabel> labels;
    private ArrayList<JSeparator> separators;

    public TransactionHistoryPanel(Database db, BankAccount selectedAccount) {
        super(25);
        ThemeManager.styleSecondaryPanel(this);

        labels = new ArrayList<>();
        separators = new ArrayList<>();

        // Transaction History Title
        labels.add(new JLabel("Transaction History"));
        labels.get(0).setBounds(20, 20, 300, 40);
        ThemeManager.styleProfileTitle(labels.get(0));

        // Description
        labels.add(new JLabel("All Transactions for this account"));
        labels.get(1).setBounds(20, 60, 400, 40);
        ThemeManager.styleProfileLabel(labels.get(1));

        labels.add(new JLabel("Transaction ID"));
        labels.get(2).setBounds(20, 100, 150, 40);
        ThemeManager.styleProfileValue(labels.get(2));

        labels.add(new JLabel("Date"));
        labels.get(3).setBounds(200, 100, 50, 40);
        ThemeManager.styleProfileValue(labels.get(3));

        labels.add(new JLabel("Type"));
        labels.get(4).setBounds(290, 100, 50, 40);
        ThemeManager.styleProfileValue(labels.get(4));

        labels.add(new JLabel("Description"));
        labels.get(5).setBounds(380, 100, 100, 40);
        ThemeManager.styleProfileValue(labels.get(5));

        labels.add(new JLabel("Amount"));
        labels.get(6).setBounds(590, 100, 100, 40);
        ThemeManager.styleProfileValue(labels.get(6));

        ArrayList<BankOperation> tempBankOperations = db.getBankAccountOperations(selectedAccount.getOperationIds());
        for (int i = 0; i < tempBankOperations.size(); i++) {
            final int INDEX = i;
            separators.add(new JSeparator(SwingConstants.HORIZONTAL));
            separators.get(i).setForeground(new Color(107, 124, 147, 95));
            separators.get(i).setBounds(20, 140 + (i * 40), 704, 1);

            labels.add(new JLabel(tempBankOperations.get(INDEX).getId()));
            labels.get(7 + INDEX * 5).setBounds(20, 140 + (i * 40), 100, 40);

            labels.add(new JLabel(tempBankOperations.get(INDEX).getDate()));
            labels.get(8 + INDEX * 5).setBounds(200, 140 + (i * 40), 100, 40);

            labels.add(new JLabel(tempBankOperations.get(INDEX).getOperationType()));
            labels.get(9 + INDEX * 5).setBounds(290, 140 + (i * 40), 100, 40);

            labels.add(new JLabel(tempBankOperations.get(INDEX).getDescription()));
            labels.get(10 + INDEX * 5).setBounds(380, 140 + (i * 40), 170, 40);

            labels.add(new JLabel(Double.toString(tempBankOperations.get(INDEX).getAmount())));
            labels.get(11 + INDEX * 5).setBounds(590, 140 + (i * 40), 100, 40);

        }

        for (JLabel l : labels) {
            add(l);
        }

        for (JSeparator sp : separators) {
            add(sp);
        }
    }
}
