package bank;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Client {

    private String id;
    private UserType userType;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private ArrayList<String> bankAccountIds;

    @JsonCreator
    public Client(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("email") String email,
            @JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("accountIds") ArrayList<String> bankAccountIds) {
        this.id = id;
        this.userType = userType;
        this.fullname = fullname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.bankAccountIds = bankAccountIds;
    }

    public void linkBankAccount(String bankAccountId) {
        this.bankAccountIds.add(bankAccountId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getBankAccountIds() {
        return bankAccountIds;
    }

    @Override
    public String toString() {
        return id + " " + fullname + " " + email + " " + username + " " + password + " " + bankAccountIds;
    }
}
