package core;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.*;

import gui.components.ModernScrollBarUI;
import gui.panels.AccountTransactionHistoryPanel;
import gui.panels.DashboardPanel;

public class PanelManager implements PanelEventListener {
    private CardLayout cLayout;
    private JPanel currentPanel;

    public PanelManager(Database db, JPanel currentPanel) {
        this.currentPanel = currentPanel;
        cLayout = (CardLayout) currentPanel.getLayout();
    }

    public void addPanel(String panelName, JPanel panel) {
        currentPanel.add(panel, panelName);
    }

    public void addPanel(String panelName, JScrollPane sp) {
        currentPanel.add(sp, panelName);
    }

    @Override
    public void onEvent(String panelName, JPanel panel, Dimension d) {
        if (d.getWidth() != 0 && d.getHeight() != 0) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(currentPanel);
            parentFrame.setSize(d);
            parentFrame.setLocationRelativeTo(null);
        }
        if (panelName.equals("dashboard")) {
            JScrollPane dashboardScroll = new JScrollPane(panel);
            dashboardScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            dashboardScroll.getVerticalScrollBar().setUnitIncrement(20);
            dashboardScroll
                    .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            dashboardScroll
                    .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            dashboardScroll.setBorder(BorderFactory.createEmptyBorder());
            DashboardPanel dp = (DashboardPanel) panel;
            int numBankAccounts = dp.getNumBankAccounts();
            if (numBankAccounts < 3) {
                dp.setPreferredSize(new Dimension(774, 650));
            } else {
                dp.setPreferredSize(new Dimension(774, 800 + 322 * ((int) Math.ceil(numBankAccounts / 2.0f) - 2)));
            }
            addPanel(panelName, dashboardScroll);
            showPanel(panelName);
            return;
        }
        if (panelName.equals("transactions")) {
            JScrollPane sp = new JScrollPane(panel);
            sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            sp.getVerticalScrollBar().setUnitIncrement(20);
            sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            sp.setBorder(BorderFactory.createEmptyBorder());
            panel.setPreferredSize(new Dimension(774, 900));
            addPanel(panelName, sp);
            showPanel(panelName);
            return;
        }
        if (panelName.equals("transactionHistory")) {
            JScrollPane sp = new JScrollPane(panel);
            sp.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            sp.getVerticalScrollBar().setUnitIncrement(20);
            sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            sp.setBorder(BorderFactory.createEmptyBorder());
            AccountTransactionHistoryPanel athp = (AccountTransactionHistoryPanel) panel;
            int numBankOperations = athp.getNumOperations();
            if (numBankOperations < 4) {
                athp.setPreferredSize(new Dimension(774, 650));
            } else {
                athp.setPreferredSize(new Dimension(774, 800 + 322 * ((numBankOperations / 2) - 2)));
            }
            addPanel(panelName, sp);
            showPanel(panelName);
            return;
        }
        if (panelName.equals("transfer")) {
            currentPanel.setBounds(10, 410, 744, 430);
            addPanel(panelName, panel);
            showPanel(panelName);
            return;
        }
        if (panelName.equals("deposit") || panelName.equals("withdraw")) {
            currentPanel.setBounds(10, 410, 744, 350);
        }
        addPanel(panelName, panel);
        showPanel(panelName);
    }

    public void showPanel(String name) {
        cLayout.show(currentPanel, name);
    }
}
