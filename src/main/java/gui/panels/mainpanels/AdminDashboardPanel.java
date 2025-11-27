package gui.panels.mainpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import bank.Admin;
import bank.BankAccount;
import bank.Client;
import bank.Teller;
import bank.User;
import core.CreateUserListener;
import core.CreateUserListener;
import core.Database;
import core.DeleteUserListener;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.ModernScrollBarUI;
import gui.components.RoundedButton;
import gui.components.RoundedTextField;
import gui.panels.subpanels.SearchResultsPanel;

public class AdminDashboardPanel extends JPanel implements DeleteUserListener, CreateUserListener {
    private PanelEventListener panelEventListener;
    private Database db;
    private Admin a;

    private List<JLabel> labels;
    private RoundedTextField searchBar;
    private SearchResultsPanel searchResultPanel;
    private final ImageIcon SEARCH_ICON = new ImageIcon(getClass().getResource("/img/search-hover-icon.png"));
    private JButton searchButton;
    private JScrollPane searchResultScroll;

    public AdminDashboardPanel(Database db, PanelEventListener panelEventListener, Admin a) {
        this.panelEventListener = panelEventListener;
        this.db = db;
        this.a = a;
        labels = new ArrayList<>();

        ThemeManager.styleMainPanel(this);

        // Title
        labels.add(new JLabel("Welcome, " + a.getUsername()));
        labels.get(0).setBounds(10, 30, 400, 30);
        ThemeManager.styleTitlePanel(labels.get(0));

        // Description
        labels.add(new JLabel("Search for a specific client or select one from the list below"));
        labels.get(1).setBounds(10, 60, 600, 40);
        ThemeManager.styleDescription(labels.get(1));

        // Search Bar
        User[] users = a.getUsers(db);
        searchBar = new RoundedTextField(false);
        searchBar.setPlaceholder("Search a client by id, full name, username, email");
        searchBar.setBounds(10, 110, 600, 40);
        ThemeManager.styleRoundedTextField(searchBar);
        addSearchActionListener(searchBar);

        searchButton = new RoundedButton("");
        searchButton.setIcon(SEARCH_ICON);
        searchButton.setBounds(620, 110, 40, 40);
        ThemeManager.styleButton(searchButton);
        addSearchActionListener(searchButton);

        searchResultPanel = new SearchResultsPanel(db, panelEventListener, a, users);
        int numUsers = users.length;
        searchResultPanel.setPreferredSize(new Dimension(1040, 420));
        if (numUsers > 7) {
            searchResultPanel.setPreferredSize(new Dimension(1040, 420 + (numUsers - 7) * 40));
        }
        searchResultScroll = new JScrollPane(searchResultPanel);
        searchResultScroll.setBounds(10, 160, 754, 420);
        searchResultScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUnitIncrement(20);
        searchResultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchResultScroll.setBorder(BorderFactory.createEmptyBorder());
        searchResultPanel.addListener(this);

        for (JLabel l : labels) {
            add(l);
        }
        add(searchBar);
        add(searchResultScroll);
        add(searchButton);
    }

    private void addSearchActionListener(JButton button) {
        button.addActionListener((ActionEvent e) -> {
            User[] users = null;
            if (e.getSource() == button) {
                users = filterResults(a.getUsers(db), searchBar.getText());
                remove(searchResultScroll);

                searchResultPanel = new SearchResultsPanel(db, panelEventListener, a, users);
                searchResultPanel.setPreferredSize(new Dimension(1040, 420));
                if (users.length > 7) {
                    searchResultPanel.setPreferredSize(new Dimension(1040, 420 + (users.length - 7) * 40));
                }
                searchResultScroll = new JScrollPane(searchResultPanel);
                searchResultScroll.setBounds(10, 160, 754, 420);
                searchResultScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
                searchResultScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                searchResultScroll.getVerticalScrollBar().setUnitIncrement(20);
                searchResultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                searchResultScroll.setBorder(BorderFactory.createEmptyBorder());
                searchResultPanel.addListener(this);
                add(searchResultScroll);
                revalidate();
                repaint();
            }
        });
    }

    private void addSearchActionListener(JTextField textField) {
        textField.addActionListener((ActionEvent e) -> {
            User[] users = null;
            if (e.getSource() == textField) {
                users = filterResults(a.getUsers(db), searchBar.getText());
                remove(searchResultScroll);

                searchResultPanel = new SearchResultsPanel(db, panelEventListener, a, users);
                searchResultPanel.setPreferredSize(new Dimension(1040, 420));
                if (users.length > 7) {
                    searchResultPanel.setPreferredSize(new Dimension(1040, 420 + (users.length - 7) * 40));
                }
                searchResultScroll = new JScrollPane(searchResultPanel);
                searchResultScroll.setBounds(10, 160, 754, 420);
                searchResultScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
                searchResultScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                searchResultScroll.getVerticalScrollBar().setUnitIncrement(20);
                searchResultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                searchResultScroll.setBorder(BorderFactory.createEmptyBorder());
                searchResultPanel.addListener(this);
                add(searchResultScroll);
                revalidate();
                repaint();
            }
        });
    }

    private Client[] filterResults(User[] clients, String text) {
        List<Client> tempList = new ArrayList<>();
        for (User user : clients) {
            if (user == null) {
                continue;
            }

            if (isMatch(user, text)) {
                Client tempClient = (Client) user;
                tempList.add(tempClient);
            }
        }
        return tempList.toArray(new Client[0]);
    }

    private boolean isMatch(User user, String query) {
        return containsIgnoreCase(user.getUsername(), query) ||
                containsIgnoreCase(user.getEmail(), query) ||
                containsIgnoreCase(user.getFullname(), query) ||
                containsIgnoreCase(String.valueOf(user.getId()), query);
    }

    private boolean containsIgnoreCase(String field, String query) {
        return field != null && field.toLowerCase().contains(query);
    }

    @Override
    public void onUserDeletion(User[] users) {
        remove(searchResultScroll);
        searchResultPanel = new SearchResultsPanel(db, panelEventListener, a, users);
        searchResultPanel.setPreferredSize(new Dimension(1040, 420));
        if (users.length > 7) {
            searchResultPanel.setPreferredSize(new Dimension(1040, 420 + (users.length - 7) * 40));
        }
        searchResultScroll = new JScrollPane(searchResultPanel);
        searchResultScroll.setBounds(10, 160, 754, 420);
        searchResultScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUnitIncrement(20);
        searchResultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchResultScroll.setBorder(BorderFactory.createEmptyBorder());
        searchResultPanel.addListener(this);
        add(searchResultScroll);
        revalidate();
        repaint();
    }

    @Override
    public void onUserCreation(User user) {

        // Search Bar
        User[] clients = a.getUsers(db);
        remove(searchBar);
        searchBar = new RoundedTextField(false);
        searchBar.setPlaceholder("Search a client by id, full name, username, email");
        searchBar.setBounds(10, 110, 600, 40);
        ThemeManager.styleRoundedTextField(searchBar);
        addSearchActionListener(searchBar);

        remove(searchButton);
        searchButton = new RoundedButton("");
        searchButton.setIcon(SEARCH_ICON);
        searchButton.setBounds(620, 110, 40, 40);
        ThemeManager.styleButton(searchButton);
        addSearchActionListener(searchButton);

        remove(searchResultPanel);
        remove(searchResultScroll);
        searchResultPanel = new SearchResultsPanel(db, panelEventListener, a, clients);
        int numClients = clients.length;
        searchResultPanel.setPreferredSize(new Dimension(1040, 420));
        if (numClients > 7) {
            searchResultPanel.setPreferredSize(new Dimension(1040, 420 + (numClients - 7) * 40));
        }
        searchResultScroll = new JScrollPane(searchResultPanel);
        searchResultScroll.setBounds(10, 160, 754, 420);
        searchResultScroll.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        searchResultScroll.getVerticalScrollBar().setUnitIncrement(20);
        searchResultScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchResultScroll.setBorder(BorderFactory.createEmptyBorder());
        searchResultPanel.addListener(this);

        add(searchBar);
        add(searchResultScroll);
        add(searchButton);
        revalidate();
        repaint();
    }
}
