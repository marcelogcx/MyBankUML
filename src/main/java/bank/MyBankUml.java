package bank;

import java.awt.CardLayout;
import javax.swing.*;
import core.*;
import gui.panels.LoginPanel;

public class MyBankUml {
    private JFrame mainFrame;
    private JPanel currentPanel;
    private PanelManager pManager;
    private Database db;

    public MyBankUml() {
        db = new Database();
        mainFrame = new JFrame();
        mainFrame.setResizable(false);
        mainFrame.setSize(500, 500);

        currentPanel = new JPanel(new CardLayout());
        pManager = new PanelManager(db, currentPanel);

        pManager.addPanel("login", new LoginPanel(db, pManager));

        pManager.showPanel("login");

        mainFrame.add(currentPanel);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

}
