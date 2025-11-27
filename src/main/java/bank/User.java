package bank;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import core.Database;
import core.PanelEventListener;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "userType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = Teller.class, name = "TELLER"),
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})
public abstract class User {
    private String id;
    private UserType userType;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private boolean isUserBlocked;

    public User(String id, UserType userType, String fullname, String email, String username, String password,
            Boolean isUserBlocked) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.userType = userType;
        this.username = username;
        this.password = password;
        this.isUserBlocked = isUserBlocked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsUserBlocked(boolean isUserBlocked) {
        this.isUserBlocked = isUserBlocked;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsUserBlocked() {
        return isUserBlocked;
    }

    public abstract JPanel createMainPanel(Database db, PanelEventListener panelEventListener);
}
