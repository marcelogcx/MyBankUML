package gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.Database;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.RoundedButton;

public class HeaderPanel extends JPanel {
    private Database db;
    private PanelEventListener listener;

    private final ImageIcon LOGO = new ImageIcon(getClass().getResource("/img/logo.png"));
    private final ImageIcon LOGOUT = new ImageIcon(getClass().getResource("/img/logout.png"));
    private JLabel title;
    private JButton logout;

    HeaderPanel(Database db, PanelEventListener listener) {
        this.db = db;
        this.listener = listener;
        ThemeManager.styleSecondaryPanel(this);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 0, 0, 40)));

        // TITLE
        title = new JLabel("MyBankUML");
        title.setIcon(LOGO);
        title.setBounds(10, 0, 300, 100);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(15, 76, 129));

        // BUTTON
        logout = new RoundedButton("Log Out", LOGOUT);
        logout.setFont(new Font("Arial", Font.BOLD, 16));
        logout.setBounds(894, 25, 120, 50);
        logout.setBackground(Color.WHITE);
        addLogoutActionListener(logout);

        add(title);
        add(logout);
    }

    private void addLogoutActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            if (e.getSource() == button) {
                LoginPanel loginPanel = new LoginPanel(db, listener);
                listener.onEvent("login", loginPanel, new Dimension(500, 500));
            }

        });
    }
}
