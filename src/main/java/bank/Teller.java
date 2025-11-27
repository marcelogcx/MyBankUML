package bank;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonProperty;

import core.Database;
import core.PanelEventListener;
import gui.panels.mainpanels.TellerMainPanel;

public class Teller extends User {

    public Teller(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname, @JsonProperty("email") String email,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password, @JsonProperty("isUserBlocked") boolean isUserBlocked) {
        super(id, userType, fullname, email, username, password, isUserBlocked);
    }

    @Override
    public JPanel createMainPanel(Database db, PanelEventListener panelEventListener) {
        return new TellerMainPanel(db, panelEventListener, this);
    }

    public User[] getClients(Database db) {
        return db.getClients(this);
    }

}
