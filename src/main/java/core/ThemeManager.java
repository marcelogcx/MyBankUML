package core;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gui.components.RoundedComboBox;
import gui.components.RoundedTextField;

public class ThemeManager {
    private static final Font TITLE_FONT = new Font("Arial Black", Font.PLAIN, 32);
    private static final Font H2_FONT = new Font("Arial Black", Font.PLAIN, 30);
    private static final Font CURRENT_BALANCE_FONT = new Font("Arial", Font.PLAIN, 32);
    private static final Font TITLE_ACCOUNT_FONT = new Font("Arial", Font.PLAIN, 30);
    private static final Font ACCOUNT_NUMBER_FONT = new Font("Arial", Font.PLAIN, 22);
    private static final Font ACCOUNT_NUMBER_LABEL_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font STATUS_VALUE_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font TEXT_FIELD_FONT = new Font("Arial", Font.PLAIN, 16);

    private static final Color MAIN_ACCENT_COLOR = new Color(15, 76, 129);
    private static final Color HYPERLINK_COLOR = new Color(0, 180, 216);
    private static final Color BUTTON_FOREGROUND = Color.WHITE;
    private static final Color SECONDARY_FOREGROUND = new Color(107, 124, 147);

    private static final Color MAIN_FOREGROUND = new Color(32, 33, 36);
    private static final Color MAIN_BACKGROUND = new Color(248, 249, 251);
    private static final Color SECONDARY_BACKGROUND = Color.WHITE;
    private static final Color ACCOUNT_TYPE_FOREGROUND = new Color(15, 76, 129);
    private static final Color ACCOUNT_TYPE_BACKGROUND = new Color(230, 242, 248);

    private static final Color CURRENT_BALANCE_COLOR = new Color(0, 180, 216);

    private static final Color SIDEBAR_BUTTON_COLOR = new Color(255, 255, 255);

    private static final Font PROFILE_TITLE_FONT = new Font("Arial", Font.BOLD, 28);
    private static final Font PROFILE_LABEL_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font PROFILE_VALUE_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font ACCOUNT_DISTRIBUTION_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font OPERATION_LABEL_FONT = new Font("Arial", Font.PLAIN, 20);

    public static void styleMainPanel(JPanel panel) {
        panel.setBackground(MAIN_BACKGROUND);
        panel.setLayout(null);
    }

    public static void styleSecondaryPanel(JPanel panel) {
        panel.setBackground(SECONDARY_BACKGROUND);
        panel.setLayout(null);
    }

    public static void styleTitle(JLabel label) {
        label.setForeground(MAIN_ACCENT_COLOR);
        label.setFont(TITLE_FONT);
    }

    public static void styleDescription(JLabel label) {
        label.setForeground(SECONDARY_FOREGROUND);
        label.setFont(PROFILE_LABEL_FONT);
    }

    public static void styleTitleBankAccount(JLabel label) {
        label.setForeground(MAIN_FOREGROUND);
        label.setFont(TITLE_ACCOUNT_FONT);
    }

    public static void styleAccountType(JLabel label) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBackground(ACCOUNT_TYPE_BACKGROUND);
        label.setForeground(ACCOUNT_TYPE_FOREGROUND);
    }

    public static void styleAccountId(JLabel accountIdLabel, JLabel accountIdValue) {
        accountIdLabel.setForeground(SECONDARY_FOREGROUND);
        accountIdLabel.setFont(ACCOUNT_NUMBER_LABEL_FONT);
        accountIdValue.setForeground(MAIN_FOREGROUND);
        accountIdValue.setFont(ACCOUNT_NUMBER_FONT);
    }

    public static void styleCurrentBalance(JLabel currentBalanceLabel, JLabel currentBalanceValue) {
        currentBalanceLabel.setForeground(SECONDARY_FOREGROUND);
        currentBalanceLabel.setFont(LABEL_FONT);
        currentBalanceValue.setForeground(CURRENT_BALANCE_COLOR);
        currentBalanceValue.setFont(CURRENT_BALANCE_FONT);
    }

    public static void styleStatus(JLabel statusLabel, JLabel statusValue) {
        statusLabel.setForeground(SECONDARY_FOREGROUND);
        statusLabel.setFont(LABEL_FONT);
        statusValue.setForeground(BUTTON_FOREGROUND);
        statusValue.setBackground(MAIN_ACCENT_COLOR);
        statusValue.setOpaque(true);
        statusValue.setFont(STATUS_VALUE_FONT);
    }

    public static void styleFieldLabel(JLabel fieldLabel) {
        fieldLabel.setForeground(MAIN_FOREGROUND);
        fieldLabel.setFont(LABEL_FONT);
    }

    public static void styleFieldHyperlinkLabel(JLabel fieldHyperlinkLabel) {
        fieldHyperlinkLabel.setForeground(SECONDARY_FOREGROUND);
        fieldHyperlinkLabel.setFont(LABEL_FONT);
    }

    public static void styleHyperlink(JLabel hyperlink) {
        hyperlink.setForeground(HYPERLINK_COLOR);
        hyperlink.setFont(LABEL_FONT);
    }

    public static void styleButton(JButton button) {
        button.setBackground(MAIN_ACCENT_COLOR);
        button.setForeground(BUTTON_FOREGROUND);
        button.setFont(BUTTON_FONT);
    }

    public static void styleRoundedTextField(RoundedTextField tf) {
        tf.setBorderColor(SECONDARY_FOREGROUND);
        tf.setBorderColorHover(new Color(141, 204, 216));
        tf.setBorderColorFocused(HYPERLINK_COLOR);
        tf.setFont(TEXT_FIELD_FONT);
    }

    public static <T> void styleRoundedComboBox(RoundedComboBox<T> cb) {
        cb.setArc(15);
        cb.setBorderColor(SECONDARY_FOREGROUND);
        cb.setHoverBorderColor(new Color(141, 204, 216));
        cb.setSelectedBorderColor(HYPERLINK_COLOR);
        cb.setFont(TEXT_FIELD_FONT);
        cb.setSelectionHoverColor(HYPERLINK_COLOR);
    }

    public static void styleTitlePanel(JLabel title) {
        title.setForeground(MAIN_ACCENT_COLOR);
        title.setFont(H2_FONT);
    }

    public static void styleSelectedButton(JButton button) {
        button.setBackground(MAIN_ACCENT_COLOR);
        button.setForeground(BUTTON_FOREGROUND);
        button.setFont(BUTTON_FONT);
    }

    public static void styleSidebarButton(JButton button) {
        button.setBackground(SIDEBAR_BUTTON_COLOR);
        button.setForeground(MAIN_FOREGROUND);
        button.setFont(BUTTON_FONT);
    }

    public static void styleOperationButton(JButton button) {
        button.setBackground(SECONDARY_BACKGROUND);
        button.setForeground(MAIN_FOREGROUND);
        button.setFont(BUTTON_FONT);
    }

    public static void styleLabel(JLabel label) {
        label.setForeground(Color.BLACK);
        label.setFont(LABEL_FONT);
    }

    public static void styleProfileLabel(JLabel label) {
        label.setForeground(SECONDARY_FOREGROUND);
        label.setFont(PROFILE_LABEL_FONT);
    }

    public static void styleProfileValue(JLabel label) {
        label.setForeground(MAIN_FOREGROUND);
        label.setFont(PROFILE_VALUE_FONT);
    }

    public static void styleProfileTitle(JLabel label) {
        label.setForeground(MAIN_FOREGROUND);
        label.setFont(PROFILE_TITLE_FONT);
    }

    public static void styleAccountDistribution(JLabel label) {
        label.setForeground(MAIN_FOREGROUND);
        label.setFont(ACCOUNT_DISTRIBUTION_FONT);
    }

    public static void styleBalance(JLabel label) {
        label.setForeground(CURRENT_BALANCE_COLOR);
        label.setFont(CURRENT_BALANCE_FONT);
    }

    public static void styleOperationLabel(JLabel label) {
        label.setForeground(MAIN_FOREGROUND);
        label.setFont(OPERATION_LABEL_FONT);
    }
}
