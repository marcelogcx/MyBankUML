package gui.panels.mainpanels;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import bank.Teller;
import core.Database;
import core.PanelEventListener;
import core.PanelManager;
import gui.components.ModernScrollBarUI;
import gui.panels.subpanels.HeaderPanel;
import gui.panels.subpanels.TellerSidebarPanel;

public class TellerMainPanel extends JPanel {
    private HeaderPanel header;
    private TellerSidebarPanel sidebar;
    private JPanel currentPanel;
    private PanelManager pManager;

    public TellerMainPanel(Database db, PanelEventListener panelEventListener, Teller t) {
        setLayout(null);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(db, currentPanel);
        currentPanel.setBounds(250, 100, 774, 668);

        header = new HeaderPanel(db, panelEventListener);
        header.setBounds(0, 0, 1024, 100);
        sidebar = new TellerSidebarPanel(db, pManager, t);
        sidebar.setBounds(0, 100, 250, 668);
        JPanel dashboardPanel = new TellerDashboardPanel(db, pManager, t);

        JScrollPane dashboardScroll = new JScrollPane(dashboardPanel);
        dashboardScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        dashboardScroll.getVerticalScrollBar().setUnitIncrement(20);
        dashboardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        dashboardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dashboardScroll.setBorder(BorderFactory.createEmptyBorder());
        dashboardPanel.setPreferredSize(new Dimension(774, 650));

        TellerProfilePanel profilePanel = new TellerProfilePanel(t);

        pManager.addPanel("dashboard", dashboardScroll);
        pManager.addPanel("profile", profilePanel);

        pManager.showPanel("dashboard");

        add(header);
        add(sidebar);
        add(currentPanel);
    }
}
