package core;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface PanelEventListener {
    void onEvent(String panelName, JPanel panel, Dimension d);
}
