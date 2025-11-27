package gui.panels.subpanels;

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

import bank.Admin;
import bank.Teller;
import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class AdminSidebarPanel extends JPanel {
    private PanelEventListener panelEventListener;
    private Database db;
    private Admin a;

    private final ImageIcon SEARCH_ICON = new ImageIcon(getClass().getResource("/img/search-icon.png"));
    private final ImageIcon SEARCH_ACCENT_ICON = new ImageIcon(getClass().getResource("/img/search-hover-icon.png"));
    private final ImageIcon PROFILE_ICON = new ImageIcon(getClass().getResource("/img/profile.png"));
    private final ImageIcon PROFILE_ACCENT_ICON = new ImageIcon(getClass().getResource("/img/profile-hover.png"));
    private JButton[] buttons;
    private int selectedButtonIndex = 0;

    public AdminSidebarPanel(Database db, PanelEventListener panelEventListener, Admin a) {
        this.panelEventListener = panelEventListener;
        this.a = a;
        this.db = db;
        ThemeManager.styleMainPanel(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] buttonNames = { "Search Clients", "Profile" };
        ImageIcon[] buttonNormalIcons = { SEARCH_ICON, PROFILE_ICON };
        ImageIcon[] buttonAccentIcons = { SEARCH_ACCENT_ICON, PROFILE_ACCENT_ICON };
        String[] redirectionPanelNames = { "dashboard", "profile" };
        this.buttons = initiateButtons(buttonNames, buttonNormalIcons, buttonAccentIcons);
        setButtonBehavior(redirectionPanelNames, buttonNormalIcons, buttonAccentIcons);

        for (JButton button : buttons) {
            add(Box.createVerticalStrut(15));
            add(button);
        }
    }

    private JButton[] initiateButtons(String[] buttonNames, ImageIcon[] buttonNormalIcons,
            ImageIcon[] buttonAccentIcons) {
        int numButtons = buttonNames.length;
        JButton[] buttons = new JButton[numButtons];
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
                            panelEventListener.onShowEvent(redirectionalPanelNames[INDEX]);
                            break;
                        case "profile":
                            panelEventListener.onShowEvent(redirectionalPanelNames[INDEX]);
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
