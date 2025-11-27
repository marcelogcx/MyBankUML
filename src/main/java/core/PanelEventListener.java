package core;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public interface PanelEventListener {
    void onShowEvent(String panelName);

    void onAddEvent(String panelName, JPanel panel, Dimension d);

    void onAddEvent(String panelName, JScrollPane sp, Dimension d);
}
