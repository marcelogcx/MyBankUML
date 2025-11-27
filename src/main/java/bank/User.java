package bank;

/**
 * Base class for all user types in the banking system.
 * Contains common authentication and identification fields.
 */
public abstract class User {

    protected String id;
    protected String username;
    protected String password;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public abstract String toString();
}
