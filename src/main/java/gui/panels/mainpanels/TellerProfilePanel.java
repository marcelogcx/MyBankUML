package gui.panels.mainpanels;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.Teller;
import bank.User;
import core.ThemeManager;
import gui.components.RoundedLabel;
import gui.components.RoundedPanel;

public class TellerProfilePanel extends JPanel {
    private JLabel[] labels;
    private JPanel personalInfoPanel;
    private JPanel membershipDetailsPanel;

    private final ImageIcon FULL_NAME_ICON = new ImageIcon(getClass().getResource("/img/fullname-icon.png"));
    private final ImageIcon MAIL_ICON = new ImageIcon(getClass().getResource("/img/mail-icon.png"));
    private final ImageIcon USERNAME_ICON = new ImageIcon(getClass().getResource("/img/username-icon.png"));
    private final ImageIcon STATUS_ICON = new ImageIcon(getClass().getResource("/img/status-icon.png"));

    private final ImageIcon MEMBER_SINCE_ICON = new ImageIcon(getClass().getResource("/img/member-since-icon.png"));
    private final ImageIcon USER_ID_ICON = new ImageIcon(getClass().getResource("/img/id-icon.png"));
    private final ImageIcon ACCOUNT_DISTRIBUTION_ICON = new ImageIcon(
            getClass().getResource("/img/account-distribution-icon.png"));

    public TellerProfilePanel(User u) {
        ThemeManager.styleMainPanel(this);

        labels = new JLabel[20];

        // Title
        labels[0] = new JLabel("My Profile");
        labels[0].setBounds(10, 10, 300, 40);
        ThemeManager.styleTitlePanel(labels[0]);

        // Description
        labels[1] = new JLabel("Your personal information and membership details.");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleDescription(labels[1]);

        initiatePersonalInfoPanel(u);
        // initiateMembershipDetailsPanel(t);

        add(labels[0]);
        add(labels[1]);
    }

    private void initiatePersonalInfoPanel(User u) {
        personalInfoPanel = new RoundedPanel(25);
        personalInfoPanel.setLayout(null);
        personalInfoPanel.setBackground(Color.WHITE);
        personalInfoPanel.setBounds(10, 100, 372, 392);

        // Personal Info Title
        labels[2] = new JLabel("Personal Information");
        ThemeManager.styleProfileTitle(labels[2]);
        labels[2].setBounds(10, 20, 300, 25);

        // Full Name Label
        labels[3] = new JLabel("Full Name");
        labels[3].setIcon(FULL_NAME_ICON);
        ThemeManager.styleProfileLabel(labels[3]);
        labels[3].setBounds(10, 75, 300, 25);

        // Full Name Value
        labels[4] = new JLabel(u.getFullname());
        ThemeManager.styleProfileValue(labels[4]);
        labels[4].setBounds(35, 110, 300, 25);

        // Email Label
        labels[5] = new JLabel("Email Address");
        labels[5].setIcon(MAIL_ICON);
        ThemeManager.styleProfileLabel(labels[5]);
        labels[5].setBounds(10, 155, 300, 25);

        // Email Value
        labels[6] = new JLabel(u.getEmail());
        ThemeManager.styleProfileValue(labels[6]);
        labels[6].setBounds(35, 190, 300, 25);

        // Username Label
        labels[7] = new JLabel("Username");
        labels[7].setIcon(USERNAME_ICON);
        ThemeManager.styleProfileLabel(labels[7]);
        labels[7].setBounds(10, 235, 300, 25);

        // Username Value
        labels[8] = new JLabel(u.getUsername());
        ThemeManager.styleProfileValue(labels[8]);
        labels[8].setBounds(35, 270, 300, 25);

        // Status Label
        labels[9] = new JLabel("Status");
        labels[9].setIcon(STATUS_ICON);
        ThemeManager.styleProfileLabel(labels[9]);
        labels[9].setBounds(10, 315, 300, 25);

        // Status Value
        labels[10] = new RoundedLabel("Active", 10);
        ThemeManager.styleProfileValue(labels[10]);
        labels[10].setBackground(new Color(15, 76, 129));
        labels[10].setForeground(Color.WHITE);
        labels[10].setBounds(35, 350, 70, 25);

        for (int i = 2; i < 11; i++) {
            personalInfoPanel.add(labels[i]);
        }
        add(personalInfoPanel);
    }

}
