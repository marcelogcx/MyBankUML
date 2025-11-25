package gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import bank.Client;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class SidebarPanel extends JPanel {
    private PanelEventListener listener;
    private Database db;
    private Client c;

    private final ImageIcon HOME_ICON = new ImageIcon(getClass().getResource("/img/home.png"));
    private final ImageIcon HOME_ACCENT_ICON = new ImageIcon(getClass().getResource("/img/home-hover.png"));
    private final ImageIcon PROFILE_ICON = new ImageIcon(getClass().getResource("/img/profile.png"));
    private final ImageIcon PROFILE_ACCENT_ICON = new ImageIcon(getClass().getResource("/img/profile-hover.png"));
    private final ImageIcon TRANSACTIONS_ICON = new ImageIcon(getClass().getResource("/img/transactions-icon.png"));
    private JButton[] buttons;
    private int selectedButtonIndex = 0;

    SidebarPanel(Database db, PanelEventListener listener, Client c) {
        this.listener = listener;
        this.c = c;
        this.db = db;
        ThemeManager.styleMainPanel(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] buttonNames = { "Dashboard", "My Profile", "Transactions" };
        ImageIcon[] buttonNormalIcons = { HOME_ICON, PROFILE_ICON, TRANSACTIONS_ICON };
        ImageIcon[] buttonAccentIcons = { HOME_ACCENT_ICON, PROFILE_ACCENT_ICON, TRANSACTIONS_ICON };
        String[] redirectionPanelNames = { "dashboard", "profile", "transactions" };
        this.buttons = initiateButtons(buttonNames, buttonNormalIcons, buttonAccentIcons);
        setButtonBehavior(redirectionPanelNames, buttonNormalIcons, buttonAccentIcons);

        for (JButton button : buttons) {
            add(Box.createVerticalStrut(15));
            add(button);
        }
    }

    private JButton[] initiateButtons(String[] buttonNames, ImageIcon[] buttonNormalIcons,
            ImageIcon[] buttonAccentIcons) {
        JButton[] buttons = new JButton[3];
        for (int i = 0; i < buttons.length; i++) {
            if (i == 0) {
                buttons[i] = new RoundedButton(buttonNames[i], buttonAccentIcons[i]);
            } else {
                buttons[i] = new RoundedButton(buttonNames[i], buttonNormalIcons[i]);
            }
        }
        for (int i = 0; i < buttons.length; i++) {
            if (i == 0) {
                ThemeManager.styleSelectedButton(buttons[i]);
            } else {
                ThemeManager.styleSidebarButton(buttons[i]);
            }
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttons[i].setMaximumSize(new Dimension(200, 40));
        }
        return buttons;
    }

    private void setButtonBehavior(String[] redirectionalPanelNames, ImageIcon[] buttonNormalIcons,
            ImageIcon[] buttonAccentIcons) {
        for (int i = 0; i < buttons.length; i++) {
            final int INDEX = i;
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    buttons[INDEX].setBackground(new Color(0, 180, 216));
                    buttons[INDEX].setForeground(Color.WHITE);
                    buttons[INDEX].setIcon(buttonAccentIcons[INDEX]);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    ThemeManager.styleSidebarButton(buttons[INDEX]);
                    buttons[INDEX].setIcon(buttonNormalIcons[INDEX]);
                    if (selectedButtonIndex == INDEX) {
                        ThemeManager.styleSelectedButton(buttons[INDEX]);
                        buttons[INDEX].setIcon(buttonAccentIcons[INDEX]);
                    }
                }
            });
            buttons[i].addActionListener((ActionEvent e) -> {
                if (e.getSource() == buttons[INDEX]) {
                    resetButtonColors(buttonNormalIcons);
                    selectedButtonIndex = INDEX;
                    ThemeManager.styleSelectedButton(buttons[INDEX]);
                    buttons[INDEX].setIcon(buttonAccentIcons[INDEX]);
                    switch (redirectionalPanelNames[INDEX]) {
                        case "dashboard":
                            DashboardPanel dp = new DashboardPanel(db, listener, c);
                            listener.onEvent(redirectionalPanelNames[INDEX], dp, new Dimension(0, 0));
                            break;
                        case "profile":
                            ProfilePanel pp = new ProfilePanel(c);
                            listener.onEvent(redirectionalPanelNames[INDEX], pp, new Dimension(0, 0));
                            break;
                        case "transactions":
                            TransactionsPanel tp = new TransactionsPanel(db, c);
                            listener.onEvent(redirectionalPanelNames[INDEX], tp, new Dimension(0, 0));
                            break;
                    }
                }
            });
        }

    }

    private void resetButtonColors(ImageIcon[] buttonNormalIcons) {
        for (int i = 0; i < buttons.length; i++) {
            ThemeManager.styleSidebarButton(buttons[i]);
            buttons[i].setIcon(buttonNormalIcons[i]);
        }
    }

}
