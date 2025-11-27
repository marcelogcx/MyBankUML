package gui.panels.mainpanels;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.Client;
import core.ThemeManager;
import gui.components.RoundedLabel;
import gui.components.RoundedPanel;
import gui.panels.subpanels.TitleDescriptionPanel;

public class ProfilePanel extends JPanel {
    private JPanel titleDescriptionPanel;
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

    public ProfilePanel(Client c) {
        ThemeManager.styleMainPanel(this);

        labels = new JLabel[18];

        // Title and Description
        titleDescriptionPanel = new TitleDescriptionPanel("My Profile",
                "Your personal information and membership details");
        titleDescriptionPanel.setBounds(10, 30, 500, 80);

        initiatePersonalInfoPanel(c);
        initiateMembershipDetailsPanel(c);

        add(titleDescriptionPanel);
    }

    private void initiatePersonalInfoPanel(Client c) {
        personalInfoPanel = new RoundedPanel(25);
        personalInfoPanel.setLayout(null);
        personalInfoPanel.setBackground(Color.WHITE);
        personalInfoPanel.setBounds(10, 120, 372, 392);

        // Personal Info Title
        labels[0] = new JLabel("Personal Information");
        ThemeManager.styleProfileTitle(labels[0]);
        labels[0].setBounds(10, 20, 300, 25);

        // Full Name Label
        labels[1] = new JLabel("Full Name");
        labels[1].setIcon(FULL_NAME_ICON);
        ThemeManager.styleProfileLabel(labels[1]);
        labels[1].setBounds(10, 75, 300, 25);

        // Full Name Value
        labels[2] = new JLabel(c.getFullname());
        ThemeManager.styleProfileValue(labels[2]);
        labels[2].setBounds(35, 110, 300, 25);

        // Email Label
        labels[3] = new JLabel("Email Address");
        labels[3].setIcon(MAIL_ICON);
        ThemeManager.styleProfileLabel(labels[3]);
        labels[3].setBounds(10, 155, 300, 25);

        // Email Value
        labels[4] = new JLabel(c.getEmail());
        ThemeManager.styleProfileValue(labels[4]);
        labels[4].setBounds(35, 190, 300, 25);

        // Username Label
        labels[5] = new JLabel("Username");
        labels[5].setIcon(USERNAME_ICON);
        ThemeManager.styleProfileLabel(labels[5]);
        labels[5].setBounds(10, 235, 300, 25);

        // Username Value
        labels[6] = new JLabel(c.getUsername());
        ThemeManager.styleProfileValue(labels[6]);
        labels[6].setBounds(35, 270, 300, 25);

        // Status Label
        labels[7] = new JLabel("Status");
        labels[7].setIcon(STATUS_ICON);
        ThemeManager.styleProfileLabel(labels[7]);
        labels[7].setBounds(10, 315, 300, 25);

        // Status Value
        if (c.getIsUserBlocked()) {
            labels[8] = new RoundedLabel("Blocked", 10);
            ThemeManager.styleProfileValue(labels[8]);
            labels[8].setBackground(Color.RED);
            labels[8].setForeground(Color.WHITE);
            labels[8].setBounds(35, 350, 90, 25);
        } else {
            labels[8] = new RoundedLabel("Active", 10);
            ThemeManager.styleProfileValue(labels[8]);
            labels[8].setBackground(new Color(15, 76, 129));
            labels[8].setForeground(Color.WHITE);
            labels[8].setBounds(35, 350, 70, 25);
        }

        for (int i = 0; i < 9; i++) {
            personalInfoPanel.add(labels[i]);
        }
        add(personalInfoPanel);
    }

    private void initiateMembershipDetailsPanel(Client c) {
        membershipDetailsPanel = new RoundedPanel(25);
        membershipDetailsPanel.setLayout(null);
        membershipDetailsPanel.setBackground(Color.WHITE);
        membershipDetailsPanel.setBounds(392, 120, 372, 392);

        // Membership Details Title
        labels[9] = new JLabel("Membership Details");
        ThemeManager.styleProfileTitle(labels[9]);
        labels[9].setBounds(10, 20, 300, 25);

        // Member Since Label
        labels[10] = new JLabel("Member Since");
        labels[10].setIcon(MEMBER_SINCE_ICON);
        ThemeManager.styleProfileLabel(labels[10]);
        labels[10].setBounds(10, 75, 300, 25);

        // Member Since Value
        labels[11] = new JLabel("2024-01-15");
        ThemeManager.styleProfileValue(labels[11]);
        labels[11].setBounds(35, 110, 300, 25);

        // User ID Label
        labels[12] = new JLabel("User ID");
        labels[12].setIcon(USER_ID_ICON);
        ThemeManager.styleProfileLabel(labels[12]);
        labels[12].setBounds(10, 155, 300, 25);

        // User ID Value
        labels[13] = new JLabel(c.getId());
        ThemeManager.styleProfileValue(labels[13]);
        labels[13].setBounds(35, 190, 300, 25);

        // Total Accounts Label
        labels[14] = new JLabel("Total Accounts");
        labels[14].setIcon(USERNAME_ICON);
        ThemeManager.styleProfileLabel(labels[14]);
        labels[14].setBounds(10, 235, 300, 25);

        // Total Accounts Value
        labels[15] = new JLabel(c.getBankAccountIds().size() + " accounts");
        ThemeManager.styleProfileValue(labels[15]);
        labels[15].setBounds(35, 270, 300, 25);

        // Total Portfolio Label
        labels[16] = new JLabel("Total Portfolio Value");
        labels[16].setIcon(ACCOUNT_DISTRIBUTION_ICON);
        ThemeManager.styleProfileLabel(labels[16]);
        labels[16].setBounds(10, 315, 300, 25);

        // Total Portfolio Value
        labels[17] = new JLabel("$4,500.00");
        ThemeManager.styleProfileValue(labels[17]);
        labels[17].setBounds(35, 350, 300, 25);

        for (int i = 9; i < 18; i++) {
            membershipDetailsPanel.add(labels[i]);
        }
        add(membershipDetailsPanel);
    }

}
