package bank;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import core.ClientListener;
import core.Database;
import core.PanelEventListener;
import gui.panels.mainpanels.ClientMainPanel;

public class Client extends User {
    private List<ClientListener> listeners = new ArrayList<>();
    private List<String> bankAccountIds;

    @JsonCreator
    public Client(@JsonProperty("id") String id, @JsonProperty("userType") UserType userType,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("email") String email,
            @JsonProperty("username") String username, @JsonProperty("password") String password,
            @JsonProperty("accountIds") ArrayList<String> bankAccountIds,
            @JsonProperty("isUserBlocked") boolean isUserBlocked) {
        super(id, userType, fullname, email, username, password, isUserBlocked);
        this.bankAccountIds = bankAccountIds;
    }

    public void addListener(ClientListener cl) {
        listeners.add(cl);
    }

    public void linkBankAccount(String bankAccountId) {
        this.bankAccountIds.add(bankAccountId);
        for (ClientListener cl : listeners) {
            cl.onAdditionBankAccount(bankAccountIds);
        }
    }

    public List<String> getBankAccountIds() {
        return bankAccountIds;
    }

    @Override
    public String toString() {
        return getId() + " " + getFullname() + " " + getEmail() + " " + getUsername() + " " + getPassword() + " "
                + bankAccountIds;
    }

    @Override
    public JPanel createMainPanel(Database db, PanelEventListener panelEventListener) {
        return new ClientMainPanel(db, panelEventListener, this);
    }

}
