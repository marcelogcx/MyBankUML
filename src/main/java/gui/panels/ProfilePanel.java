package gui.panels;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.Client;
import core.ThemeManager;
import gui.components.RoundedLabel;
import gui.components.RoundedPanel;

public class ProfilePanel extends JPanel {
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

    ProfilePanel(Client c) {
        ThemeManager.styleMainPanel(this);

        labels = new JLabel[20];

        // Title
        labels[0] = new JLabel("My Profile");
        labels[0].setBounds(10, 10, 300, 40);
        ThemeManager.styleH2(labels[0]);

        // Description
        labels[1] = new JLabel("Your personal information and membership details.");
        labels[1].setBounds(10, 50, 500, 40);
        ThemeManager.styleDescription(labels[1]);

        initiatePersonalInfoPanel(c);
        initiateMembershipDetailsPanel(c);

        add(labels[0]);
        add(labels[1]);
    }

    private void initiatePersonalInfoPanel(Client c) {
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
        labels[4] = new JLabel(c.getFullname());
        ThemeManager.styleProfileValue(labels[4]);
        labels[4].setBounds(35, 110, 300, 25);

        // Email Label
        labels[5] = new JLabel("Email Address");
        labels[5].setIcon(MAIL_ICON);
        ThemeManager.styleProfileLabel(labels[5]);
        labels[5].setBounds(10, 155, 300, 25);

        // Email Value
        labels[6] = new JLabel(c.getEmail());
        ThemeManager.styleProfileValue(labels[6]);
        labels[6].setBounds(35, 190, 300, 25);

        // Username Label
        labels[7] = new JLabel("Username");
        labels[7].setIcon(USERNAME_ICON);
        ThemeManager.styleProfileLabel(labels[7]);
        labels[7].setBounds(10, 235, 300, 25);

        // Username Value
        labels[8] = new JLabel(c.getUsername());
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

    private void initiateMembershipDetailsPanel(Client c) {
        membershipDetailsPanel = new RoundedPanel(25);
        membershipDetailsPanel.setLayout(null);
        membershipDetailsPanel.setBackground(Color.WHITE);
        membershipDetailsPanel.setBounds(392, 100, 372, 392);

        // Membership Details Title
        labels[11] = new JLabel("Membership Details");
        ThemeManager.styleProfileTitle(labels[11]);
        labels[11].setBounds(10, 20, 300, 25);

        // Member Since Label
        labels[12] = new JLabel("Member Since");
        labels[12].setIcon(MEMBER_SINCE_ICON);
        ThemeManager.styleProfileLabel(labels[12]);
        labels[12].setBounds(10, 75, 300, 25);

        // Member Since Value
        labels[13] = new JLabel("2024-01-15");
        ThemeManager.styleProfileValue(labels[13]);
        labels[13].setBounds(35, 110, 300, 25);

        // User ID Label
        labels[14] = new JLabel("User ID");
        labels[14].setIcon(USER_ID_ICON);
        ThemeManager.styleProfileLabel(labels[14]);
        labels[14].setBounds(10, 155, 300, 25);

        // User ID Value
        labels[15] = new JLabel(c.getId());
        ThemeManager.styleProfileValue(labels[15]);
        labels[15].setBounds(35, 190, 300, 25);

        // Total Accounts Label
        labels[16] = new JLabel("Total Accounts");
        labels[16].setIcon(USERNAME_ICON);
        ThemeManager.styleProfileLabel(labels[16]);
        labels[16].setBounds(10, 235, 300, 25);

        // Total Accounts Value
        labels[17] = new JLabel(c.getBankAccountIds().size() + " accounts");
        ThemeManager.styleProfileValue(labels[17]);
        labels[17].setBounds(35, 270, 300, 25);

        for (int i = 11; i < 18; i++) {
            membershipDetailsPanel.add(labels[i]);
        }
        add(membershipDetailsPanel);
    }

}
