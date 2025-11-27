package bank;

import core.Database;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Admin extends User {

    private Database db;

    @JsonCreator
    public Admin(@JsonProperty("adminID") String adminID,
                 @JsonProperty("username") String username,
                 @JsonProperty("password") String password) {
        super(adminID, username, password);
    }

    public void setDatabase(Database db) {
        this.db = db;
    }




    public void signout() {
        System.out.println("Admin " + username + " signed out successfully");
    }

    public boolean grantAccess(String userId, UserType newUserType) {
        if (db == null) {
            System.err.println("Database not initialized");
            return false;
        }
        Client user = db.readRecord(Client.class, userId);
        if (user == null) {
            System.err.println("User not found");
            return false;
        }
        user.setUserType(newUserType);
        System.out.println("Access granted: User " + userId + " promoted to " + newUserType);
        return true;
    }

    public java.util.ArrayList<Client> getAllUsers() {
        if (db == null) {
            System.err.println("Database not initialized");
            return new java.util.ArrayList<>();
        }
        return db.getAllUsers();
    }

    public String getAdminID() {
        return id;
    }

    @Override
    public String toString() {
        return "Admin{adminID='" + id + "', username='" + username + "'}";
    }
}
