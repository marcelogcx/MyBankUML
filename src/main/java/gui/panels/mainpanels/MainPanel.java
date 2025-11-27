package gui.panels.mainpanels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bank.Client;
import core.Database;
import core.PanelEventListener;
import core.PanelManager;
import gui.components.ModernScrollBarUI;
import gui.panels.subpanels.HeaderPanel;
import gui.panels.subpanels.SidebarPanel;

public class MainPanel extends JPanel {
    private HeaderPanel header;
    private SidebarPanel sidebar;
    private JPanel currentPanel;
    private PanelManager pManager;
    private JScrollPane dashboardScroll;

    public MainPanel(Database db, PanelEventListener listener, Client c) {
        setBackground(Color.WHITE);
        setLayout(null);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(db, currentPanel);
        currentPanel.setBounds(250, 100, 774, 668);

        header = new HeaderPanel(db, listener);
        header.setBounds(0, 0, 1024, 100);
        sidebar = new SidebarPanel(db, pManager, c);
        sidebar.setBounds(0, 100, 250, 668);

        JPanel dashboardPanel = new ClientDashboardPanel(db, pManager, c);
        dashboardScroll = new JScrollPane(dashboardPanel);
        dashboardScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(20);
        dashboardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dashboardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dashboardScroll.setBorder(BorderFactory.createEmptyBorder());
        int numBankAccounts = c.getBankAccountIds().size();
        if (numBankAccounts < 3) {
            dashboardPanel.setPreferredSize(new Dimension(774, 650));
        } else {
            dashboardPanel.setPreferredSize(new Dimension(774, 800 + 322 * ((numBankAccounts / 2) - 2)));
        }

        pManager.addPanel("dashboard", dashboardScroll);

        pManager.showPanel("dashboard");

        add(header);
        add(sidebar);
        add(currentPanel);
    }
}
