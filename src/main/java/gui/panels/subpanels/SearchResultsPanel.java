package gui.panels.subpanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import bank.User;
import bank.UserType;
import bank.Admin;
import bank.Client;
import bank.Teller;
import core.AddNewAccountListener;
import core.ClientListener;
import core.Database;
import core.DeleteUserListener;
import core.PanelEventListener;
import core.ThemeManager;
import gui.components.ModernScrollBarUI;
import gui.components.RoundedLabel;
import gui.components.RoundedPanel;
import gui.panels.mainpanels.ClientDashboardPanel;

public class SearchResultsPanel extends RoundedPanel implements ClientListener {
    private DeleteUserListener deleteUserListener;
    private User[] users;
    private User loggedUser;
    private JLabel[] labels;
    private JSeparator[] separators;
    private JPanel[] clickablePanels;
    private Database db;

    public SearchResultsPanel(Database db, PanelEventListener panelEventListener, User loggedUser,
            User[] users) {
        super(25);
        this.users = users;
        this.loggedUser = loggedUser;
        this.db = db;

        ThemeManager.styleSecondaryPanel(this);

        int numUsers;
        if (users == null) {
            numUsers = 0;
        }
        numUsers = users.length;
        labels = new JLabel[8 + numUsers * 7];
        separators = new JSeparator[numUsers];
        clickablePanels = new JPanel[numUsers];

        labels[0] = new JLabel("Search Results");
        labels[0].setBounds(20, 20, 400, 40);
        ThemeManager.styleProfileTitle(labels[0]);

        labels[1] = new JLabel("User ID");
        labels[1].setBounds(20, 70, 100, 40);
        ThemeManager.styleProfileValue(labels[1]);

        labels[2] = new JLabel("Full Name");
        labels[2].setBounds(130, 70, 100, 40);
        ThemeManager.styleProfileValue(labels[2]);

        labels[3] = new JLabel("Username");
        labels[3].setBounds(280, 70, 100, 40);
        ThemeManager.styleProfileValue(labels[3]);

        labels[4] = new JLabel("Email");
        labels[4].setBounds(450, 70, 100, 40);
        ThemeManager.styleProfileValue(labels[4]);

        labels[5] = new JLabel("Type");
        labels[5].setBounds(620, 70, 100, 40);
        ThemeManager.styleProfileValue(labels[5]);

        labels[6] = new JLabel("Status");
        labels[6].setBounds(760, 70, 150, 40);
        ThemeManager.styleProfileValue(labels[6]);

        labels[7] = new JLabel("Total Accounts");
        labels[7].setBounds(880, 70, 150, 40);
        ThemeManager.styleProfileValue(labels[7]);

        if (loggedUser.getUserType() == UserType.TELLER) {
            listTellerResults(panelEventListener);
        } else {
            listAdminResults(panelEventListener);
        }

        for (int i = 0; i < 8; i++) {
            add(labels[i]);
        }

        for (JSeparator sp : separators) {
            add(sp);
        }
        for (JPanel p : clickablePanels) {
            add(p);
        }
    }

    private void listTellerResults(PanelEventListener panelEventListener) {
        int numUsers = users.length;
        for (int i = 0; i < numUsers; i++) {
            final int INDEX = i;
            Client c = (Client) users[i];

            c.addClientListener(SearchResultsPanel.this);
            separators[i] = new JSeparator(SwingConstants.HORIZONTAL);
            separators[i].setForeground(new Color(107, 124, 147, 95));
            separators[i].setBounds(20, 110 + (i * 40), 994, 1);

            clickablePanels[i] = new JPanel();
            clickablePanels[i].setBounds(20, 110 + (i * 40), 994, 40);
            clickablePanels[i].setLayout(null);
            clickablePanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    ClientDashboardPanel cdp = new ClientDashboardPanel(db, panelEventListener, c);
                    c.setDatabase(db);
                    cdp.setTitleDescription(c.getFullname() + " - " + c.getUsername(),
                            "Here's an overview of the client's accounts");
                    JScrollPane dashboardScroll = new JScrollPane(cdp);
                    dashboardScroll.getVerticalScrollBar().setUI(new ModernScrollBarUI());
                    dashboardScroll.getVerticalScrollBar().setUnitIncrement(20);
                    dashboardScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                    dashboardScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    dashboardScroll.setBorder(BorderFactory.createEmptyBorder());
                    int numBankAccounts = c.getBankAccountIds().size();
                    if (numBankAccounts < 3) {
                        cdp.setPreferredSize(new Dimension(774, 650));
                    } else {
                        cdp.setPreferredSize(
                                new Dimension(774, 800 + 322 * ((int) Math.ceil(numBankAccounts / 2.f) - 2)));
                    }
                    c.addClientListener(cdp);
                    panelEventListener.onAddEvent("clientBankAccounts", dashboardScroll, new Dimension(0, 0));
                }
            });
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem deleteItem = new JMenuItem("Delete");
            JMenuItem blockItem = new JMenuItem("Block");
            if (users[i].getIsUserBlocked() == true) {
                blockItem.setText("Unblock");
            }
            deleteItem.addActionListener(e -> {
                db.deleteRecord(User.class, c.getId());
                db.saveFiles();
                users[INDEX] = null;
                User[] tempUsers = Arrays.stream(users).filter(Objects::nonNull).toArray(User[]::new);
                deleteUserListener.onUserDeletion(tempUsers);

            });
            blockItem.addActionListener(e -> {
                users[INDEX].setIsUserBlocked(!users[INDEX].getIsUserBlocked());
                deleteUserListener.onUserDeletion(users);
                db.saveFiles();
            });
            popupMenu.add(deleteItem);
            popupMenu.add(blockItem);
            clickablePanels[i].setComponentPopupMenu(popupMenu);

            labels[8 + i * 7] = new JLabel(users[i].getId());
            labels[8 + i * 7].setBounds(0, 0, 100, 40);
            clickablePanels[i].add(labels[8 + i * 7]);

            labels[9 + i * 7] = new JLabel(users[i].getFullname());
            labels[9 + i * 7].setBounds(110, 0, 100, 40);
            clickablePanels[i].add(labels[9 + i * 7]);

            labels[10 + i * 7] = new JLabel(users[i].getUsername());
            labels[10 + i * 7].setBounds(260, 0, 100, 40);
            clickablePanels[i].add(labels[10 + i * 7]);

            labels[11 + i * 7] = new JLabel(users[i].getEmail());
            labels[11 + i * 7].setBounds(430, 0, 170, 40);
            clickablePanels[i].add(labels[11 + i * 7]);

            labels[12 + i * 7] = new JLabel(users[i].getUserType().toString());
            labels[12 + i * 7].setBounds(600, 0, 170, 40);
            clickablePanels[i].add(labels[12 + i * 7]);

            if (users[i].getIsUserBlocked() == true) {
                labels[13 + i * 7] = new RoundedLabel("Blocked", 15);
                labels[13 + i * 7].setBackground(Color.RED);
                labels[13 + i * 7].setForeground(Color.WHITE);
            } else {
                labels[13 + i * 7] = new RoundedLabel("Active", 15);
                labels[13 + i * 7].setBackground(new Color(15, 76, 129));
                labels[13 + i * 7].setForeground(Color.WHITE);
            }
            labels[13 + i * 7].setBounds(740, 5, 65, 30);
            clickablePanels[i].add(labels[13 + i * 7]);

            if (c.getBankAccountIds() != null) {
                labels[14 + i * 7] = new JLabel(Integer.toString(c.getBankAccountIds().size()));

            } else {
                labels[14 + i * 7] = new JLabel("0");

            }
            labels[14 + i * 7].setBounds(920, 0, 100, 40);

            clickablePanels[i].add(labels[14 + i * 7]);
        }
    }

    private void listAdminResults(PanelEventListener panelEventListener) {
        int numUsers = users.length;
        for (int i = 0; i < numUsers; i++) {
            final int INDEX = i;

            separators[i] = new JSeparator(SwingConstants.HORIZONTAL);
            separators[i].setForeground(new Color(107, 124, 147, 95));
            separators[i].setBounds(20, 110 + (i * 40), 994, 1);

            clickablePanels[i] = new JPanel();
            clickablePanels[i].setBounds(20, 110 + (i * 40), 994, 40);
            clickablePanels[i].setLayout(null);
            clickablePanels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    UserType[] choices = UserType.values();

                    UserType selected = (UserType) JOptionPane.showInputDialog(
                            null, // Parent component (can be your JFrame)
                            "Select a User Type:", // Message
                            "Change Permissions", // Title of the window
                            JOptionPane.QUESTION_MESSAGE, // Icon type
                            null, // Icon (null = use default)
                            choices, // The array of options
                            choices[0] // The default selected option
                    );

                    // Check if user clicked Cancel
                    if (selected == null) {
                        System.out.println("User cancelled");
                        return;
                    }
                    if (selected == users[INDEX].getUserType()) {
                        JOptionPane.showMessageDialog(null, "User already is this type", "Failure",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    Admin tempAdmin = (Admin) loggedUser;
                    tempAdmin.grantAccess(db, users[INDEX].getId(), selected);
                    String id = db.getIdFromUsername(users[INDEX].getUsername());
                    User tempUser = db.readRecord(User.class, id);
                    users[INDEX] = tempUser;

                    db.saveFiles();
                    deleteUserListener.onUserDeletion(users);
                }
            });
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem deleteItem = new JMenuItem("Delete");
            JMenuItem blockItem = new JMenuItem("Block");
            if (users[i].getIsUserBlocked() == true) {
                blockItem.setText("Unblock");
            }
            deleteItem.addActionListener(e -> {
                db.deleteRecord(User.class, users[INDEX].getId());
                db.saveFiles();
                users[INDEX] = null;
                User[] tempUsers = Arrays.stream(users).filter(Objects::nonNull).toArray(User[]::new);
                deleteUserListener.onUserDeletion(tempUsers);
            });
            blockItem.addActionListener(e -> {
                users[INDEX].setIsUserBlocked(!users[INDEX].getIsUserBlocked());
                deleteUserListener.onUserDeletion(users);
                db.saveFiles();
            });
            popupMenu.add(deleteItem);
            popupMenu.add(blockItem);
            clickablePanels[i].setComponentPopupMenu(popupMenu);

            labels[8 + i * 7] = new JLabel(users[i].getId());
            labels[8 + i * 7].setBounds(0, 0, 100, 40);
            clickablePanels[i].add(labels[8 + i * 7]);

            labels[9 + i * 7] = new JLabel(users[i].getFullname());
            labels[9 + i * 7].setBounds(110, 0, 100, 40);
            clickablePanels[i].add(labels[9 + i * 7]);

            labels[10 + i * 7] = new JLabel(users[i].getUsername());
            labels[10 + i * 7].setBounds(260, 0, 100, 40);
            clickablePanels[i].add(labels[10 + i * 7]);

            labels[11 + i * 7] = new JLabel(users[i].getEmail());
            labels[11 + i * 7].setBounds(430, 0, 170, 40);
            clickablePanels[i].add(labels[11 + i * 7]);

            labels[12 + i * 7] = new JLabel(users[i].getUserType().toString());
            labels[12 + i * 7].setBounds(600, 0, 170, 40);
            clickablePanels[i].add(labels[12 + i * 7]);

            if (users[i].getIsUserBlocked() == true) {
                labels[13 + i * 7] = new RoundedLabel("Blocked", 15);
                labels[13 + i * 7].setBackground(Color.RED);
                labels[13 + i * 7].setForeground(Color.WHITE);
            } else {
                labels[13 + i * 7] = new RoundedLabel("Active", 15);
                labels[13 + i * 7].setBackground(new Color(15, 76, 129));
                labels[13 + i * 7].setForeground(Color.WHITE);
            }
            labels[13 + i * 7].setBounds(740, 5, 65, 30);
            clickablePanels[i].add(labels[13 + i * 7]);

            if (users[i] instanceof Client) {
                Client c = (Client) users[i];
                if (c.getBankAccountIds() != null) {
                    labels[14 + i * 7] = new JLabel(Integer.toString(c.getBankAccountIds().size()));
                } else {
                    labels[14 + i * 7] = new JLabel("0");
                }
            } else {
                labels[14 + i * 7] = new JLabel("0");
            }

            labels[14 + i * 7].setBounds(920, 0, 100, 40);
            clickablePanels[i].add(labels[14 + i * 7]);
        }
    }

    public void addDeleteListener(DeleteUserListener dul) {
        deleteUserListener = dul;
    }

    @Override
    public void onAdditionBankAccount(List<String> bankAccountIds, String id) {
        for (int i = 0; i < users.length; i++) {
            if (users[i].getId() == id) {

                labels[14 + i * 7].setText(Integer.toString(Integer.parseInt(labels[14 + i * 7].getText()) + 1));
                return;
            }
        }
        repaint();
    }
}
