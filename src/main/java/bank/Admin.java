package bank;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;
import core.PanelEventListener;
import gui.panels.mainpanels.AdminMainPanel;

public class Admin extends User {

    public Admin(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname, @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password, @JsonProperty("isUserBlocked") boolean isUserBlocked) {
        super(id, userType, fullname, email, username, password, isUserBlocked);
    }

    @Override
    public JPanel createMainPanel(Database db, PanelEventListener panelEventListener) {
        return new AdminMainPanel(db, panelEventListener, this);
    }

    public User[] getUsers(Database db) {
        if (db == null) {
            System.err.println("Database not provided.");
            return null;
        }
        return db.getClientsAndTellers(this);
    }

    public void signout() {
        System.out.println("Admin " + getUsername() + " signed out successfully");
    }

    public boolean grantAccess(Database db, String userId, UserType newUserType) {
        if (db == null) {
            return false;
        }
        User user = db.readRecord(User.class, userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }
        UserType oldUserType = user.getUserType();
        switch (newUserType) {
            case CLIENT:
                db.deleteRecord(User.class, user.getId());
                user = db.writeRecord(Client.class, new String[] { user.getFullname(),
                        user.getEmail(), user.getUsername(), user.getPassword() });
                System.out.println(user.getId());
                break;
            case TELLER:
                db.deleteRecord(User.class, user.getId());
                user = db.writeRecord(Teller.class, new String[] { user.getFullname(),
                        user.getEmail(), user.getUsername(), user.getPassword() });
                break;
            case ADMIN:
                db.deleteRecord(User.class, user.getId());
                user = db.writeRecord(Admin.class, new String[] { user.getFullname(),
                        user.getEmail(), user.getUsername(), user.getPassword() });
                break;
        }
        System.out.println(
                "Change permision to username: " + user.getUsername() + " from " + oldUserType + " to "
                        + user.getUserType());
        return true;
    }

    @Override
    public String toString() {
        return "Admin{adminID='" + getId() + "', username='" + getUsername() + "'}";
    }
}
