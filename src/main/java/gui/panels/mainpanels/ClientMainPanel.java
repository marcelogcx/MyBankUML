package gui.panels.mainpanels;

import java.awt.CardLayout;
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

public class ClientMainPanel extends JPanel {
    private HeaderPanel header;
    private SidebarPanel sidebar;
    private JPanel currentPanel;
    private PanelManager pManager;

    public ClientMainPanel(Database db, PanelEventListener panelEventListener, Client c) {
        setLayout(null);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(db, currentPanel);
        currentPanel.setBounds(250, 100, 774, 668);

        header = new HeaderPanel(db, panelEventListener);
        header.setBounds(0, 0, 1024, 100);
        sidebar = new SidebarPanel(db, pManager, c);
        sidebar.setBounds(0, 100, 250, 668);

        ClientDashboardPanel dashboardPanel = new ClientDashboardPanel(db, pManager, c);
        JScrollPane dashboardScroll = new JScrollPane(dashboardPanel);
        dashboardScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(20);
        dashboardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dashboardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dashboardScroll.setBorder(BorderFactory.createEmptyBorder());
        int numBankAccounts = c.getBankAccountIds().size();
        if (numBankAccounts < 3) {
            dashboardPanel.setPreferredSize(new Dimension(774, 650));
        } else {
            dashboardPanel
                    .setPreferredSize(new Dimension(774, 800 + 322 * ((int) Math.ceil(numBankAccounts / 2.f) - 2)));
        }
        c.addClientListener(dashboardPanel);

        TransactionsPanel tp = new TransactionsPanel(db, c);
        JScrollPane transactionsScroll = new JScrollPane(tp);
        transactionsScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        transactionsScroll.getVerticalScrollBar().setUnitIncrement(20);
        transactionsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        transactionsScroll.setBorder(BorderFactory.createEmptyBorder());
        tp.setPreferredSize(new Dimension(774, 900));

        ProfilePanel pp = new ProfilePanel(c);

        pManager.addPanel("dashboard", dashboardScroll);
        pManager.addPanel("profile", pp);
        pManager.addPanel("transactions", transactionsScroll);

        pManager.showPanel("dashboard");

        add(header);
        add(sidebar);
        add(currentPanel);
    }
}
