package core;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.*;

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
    public void onAddEvent(String panelName, JPanel panel, Dimension d) {
        if (d.getWidth() != 0 && d.getHeight() != 0) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(currentPanel);
            parentFrame.setSize(d);
            parentFrame.setLocationRelativeTo(null);
        }
        if (panelName.equals("dashboard")) {
            showPanel(panelName);
            return;
        }
        if (panelName.equals("profile")) {
            showPanel(panelName);
            return;
        }
        if (panelName.equals("transactions")) {
            showPanel(panelName);
            return;
        }
        if (panelName.equals("transactionHistory")) {

            showPanel(panelName);
            return;
        }
        if (panelName.equals("transfer")) {
            addPanel(panelName, panel);
            showPanel(panelName);
            return;
        }
        if (panelName.equals("deposit") || panelName.equals("withdraw")) {
        }
        addPanel(panelName, panel);
        showPanel(panelName);
    }

    @Override
    public void onAddEvent(String panelName, JScrollPane sp, Dimension d) {
        addPanel(panelName, sp);
        showPanel(panelName);
    }

    @Override
    public void onShowEvent(String panelName) {
        showPanel(panelName);
    }

    public void showPanel(String name) {
        cLayout.show(currentPanel, name);
    }

}
