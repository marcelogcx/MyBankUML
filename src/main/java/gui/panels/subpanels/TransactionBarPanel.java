package gui.panels.subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JPanel;

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

    public TransactionBarPanel(PanelEventListener listener, Database db, BankAccount selectedAccount,
            JPanel currentPanel) {
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
        setButtonBehavior(redirectionalPanelNames, db, selectedAccount, currentPanel);
    }

    private void setButtonBehavior(String[] redirectionalPanelNames, Database db, BankAccount selectedAccount,
            JPanel currentPanel) {
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
                            currentPanel.setSize(744, 350);
                            listener.onShowEvent(redirectionalPanelNames[INDEX]);
                            break;
                        case "withdraw":
                            currentPanel.setSize(744, 350);
                            listener.onShowEvent(redirectionalPanelNames[INDEX]);
                            break;
                        case "transfer":
                            currentPanel.setSize(744, 430);
                            listener.onShowEvent(redirectionalPanelNames[INDEX]);
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
