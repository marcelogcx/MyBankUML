package bank;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Database;
import core.PanelManager;
import gui.panels.mainpanels.LoginPanel;

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
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
