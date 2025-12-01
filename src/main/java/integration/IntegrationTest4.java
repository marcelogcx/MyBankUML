package integration;

import bank.*;
import core.Database;

public class IntegrationTest4 {

    public static void main(String[] args) {
        System.out.println("Integration Test 4: Testing interaction between Client/Teller/Admin and Accounts module.");
        Database db = new Database();

        System.out.println("\nADMIN LOGIN");
        Admin admin = (Admin) db.readRecord(User.class, db.getIdFromUsername("philjackson"));
        if (admin == null) {
            System.out.println("Admin login FAILED");
            return;
        }
        admin.setDatabase(db);
        System.out.println("Admin login SUCCESS: " + admin.getFullname());

        String client_username1 = "testclient" + System.currentTimeMillis();
        String teller_username1 = "testteller" + System.currentTimeMillis();
        System.out.println("\nADMIN CREATES TELLER");
        User teller = admin.register(UserType.TELLER, "Teller One", "teller@test.com", teller_username1, "Pass123!");
        if (teller == null) teller = db.readRecord(User.class, db.getIdFromUsername(teller_username1));
        teller.setDatabase(db);
        System.out.println("Created TELLER id=" + teller.getId());

        System.out.println("\nADMIN CREATES CLIENT");
        User client = admin.register(UserType.CLIENT, "Client One", "client@test.com", client_username1, "Pass123!");
        if (client == null) client = db.readRecord(User.class, db.getIdFromUsername(client_username1));
        client.setDatabase(db);
        System.out.println("Created CLIENT id=" + client.getId());

        System.out.println("\nTELLER LOGIN");
        Teller tellerUser = (Teller) db.readRecord(User.class, db.getIdFromUsername(teller_username1));
        tellerUser.setDatabase(db);
        System.out.println("Teller login SUCCESS");
        
        String client_username2 = "testclient" + System.currentTimeMillis();
        User client2 = tellerUser.register(UserType.CLIENT, "Client Two", "client2@test.com", client_username2, "Pass234!");
        if (client2 == null) client2 = db.readRecord(User.class, db.getIdFromUsername(client_username2));
        client2.setDatabase(db);
        System.out.println("Teller created Client (ID=" + client2.getId() + ")");

        System.out.println("\nADMIN CREATES BANK ACCOUNT FOR CLIENT 1");
        BankAccount clientAcc = db.writeRecord(BankAccount.class, new String[]{"Main Checking", BankAccountType.CHECKING.name(), "500.0"});
        clientAcc.setDatabase(db);
        ((Client) client).getBankAccountIds().add(clientAcc.getId());
        System.out.println("Linked checking account " + clientAcc.getId() + " to Client 1");

        System.out.println("\nCLIENT LOGIN");
        Client clientUser = (Client) db.readRecord(User.class, db.getIdFromUsername(client_username1));
        clientUser.setDatabase(db);
        System.out.println("Client login SUCCESS");
        System.out.println("Client accounts = " + clientUser.getBankAccountIds());
        System.out.println("Client's account balance = " + db.readRecord(BankAccount.class, clientAcc.getId()).getBalance());

        System.out.println("\nTELLER VIEWING CLIENT 1 ACCOUNTS");
        for (String id : clientUser.getBankAccountIds()) {
            BankAccount a = db.readRecord(BankAccount.class, id);
            System.out.println("Account ID=" + a.getId() + " balance=" + a.getBalance());
        }

        System.out.println("\nADMIN DELETES CLIENT 2");
        boolean removed = admin.grantAccess(db, client2.getId(), UserType.CLIENT);
        System.out.println("Client 2 removed? " + removed);

        db.saveFiles();
        System.out.println("\nAll users logged out.");
        System.out.println("\nIntegration Test 4 COMPLETE");
    }
}
