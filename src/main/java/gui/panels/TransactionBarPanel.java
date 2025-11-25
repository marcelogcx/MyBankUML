package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;

import bank.BankAccount;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;
import gui.components.RoundedPanel;

public class TransactionBarPanel extends RoundedPanel {
    private PanelEventListener listener;
    JButton[] buttons;
    private int selectedButtonIndex = 0;

    TransactionBarPanel(PanelEventListener listener, Database db, BankAccount selectedAccount) {
        super(15);
        this.listener = listener;

        ThemeManager.styleSecondaryPanel(this);

        buttons = new JButton[3];
        String[] transactionButtonNames = { "Deposit", "Withdraw", "Transfer" };
        String[] redirectionalPanelNames = { "deposit", "withdraw", "transfer" };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new RoundedButton(transactionButtonNames[i], true);
            buttons[i].setBounds(244 * i + 5, 5, 244, 30);
            ThemeManager.styleOperationButton(buttons[i]);
            add(buttons[i]);
        }
        ThemeManager.styleSelectedButton(buttons[0]);
        setButtonBehavior(redirectionalPanelNames, db, selectedAccount);
    }

    private void setButtonBehavior(String[] redirectionalPanelNames, Database db, BankAccount selectedAccount) {
        for (int i = 0; i < buttons.length; i++) {
            final int INDEX = i;
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    buttons[INDEX].setBackground(new Color(0, 180, 216));
                    buttons[INDEX].setForeground(Color.WHITE);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    ThemeManager.styleOperationButton(buttons[INDEX]);
                    if (selectedButtonIndex == INDEX) {
                        ThemeManager.styleSelectedButton(buttons[INDEX]);
                    }
                }
            });
            buttons[i].addActionListener((ActionEvent e) -> {
                if (e.getSource() == buttons[INDEX]) {
                    resetButtonColors();
                    selectedButtonIndex = INDEX;
                    ThemeManager.styleSelectedButton(buttons[INDEX]);
                    switch (redirectionalPanelNames[INDEX]) {
                        case "deposit":
                            DepositPanel dp = new DepositPanel(db, selectedAccount);
                            listener.onEvent(redirectionalPanelNames[INDEX], dp, new Dimension(0, 0));
                            break;
                        case "withdraw":
                            WithdrawPanel wp = new WithdrawPanel(db, selectedAccount);
                            listener.onEvent(redirectionalPanelNames[INDEX], wp, new Dimension(0, 0));
                            break;
                        case "transfer":
                            TransferPanel tp = new TransferPanel(db, selectedAccount);
                            listener.onEvent(redirectionalPanelNames[INDEX], tp, new Dimension(0, 0));
                            break;
                    }
                }
            });
        }

    }

    private void resetButtonColors() {
        for (int i = 0; i < buttons.length; i++) {
            ThemeManager.styleOperationButton(buttons[i]);
        }
    }

}
