package integration;

import bank.*;
import core.Database;
import java.util.Arrays;

public class IntegrationTest5 {

    public static void main(String[] args) {
        System.out.println("Integration Test 5: Testing interaction between Accounts and Database modules.");
        Database db = new Database();

        System.out.println("\nADMIN LOGIN");
        Admin admin = (Admin) db.readRecord(User.class, db.getIdFromUsername("admin"));
        if (admin == null) {
            System.out.println("Admin login FAILED");
            return;
        }
        admin.setDatabase(db);
        System.out.println("Admin login SUCCESS");

        System.out.println("\nADMIN CREATES USERS");
        User u1 = admin.register(UserType.CLIENT, "DB Test Client", "dbclient@test.com", "dbc1", "test1");
        if (u1 == null) u1 = db.readRecord(User.class, db.getIdFromUsername("dbc1"));
        u1.setDatabase(db);

        User u2 = admin.register(UserType.TELLER, "DB Test Teller", "dbteller@test.com", "dbt1", "test2");
        if (u2 == null) u2 = db.readRecord(User.class, db.getIdFromUsername("dbt1"));
        u2.setDatabase(db);

        System.out.println("Created Client ID = " + u1.getId());
        System.out.println("Created Teller ID = " + u2.getId());

        System.out.println("\nADMIN UPDATES USER TYPE");
        admin.grantAccess(db, u1.getId(), UserType.TELLER);
        User updated = db.readRecord(User.class, u1.getId());
        if (updated != null) {
            System.out.println("Updated type = " + updated.getUserType());
        } else {
            System.out.println("Error: Updated user not found in database!");
        }

        System.out.println("\nVERIFY DATABASE CONTENTS");
        User[] allUsersArray = db.getClientsAndTellers(admin);
        Arrays.stream(allUsersArray).forEach(usr ->
                System.out.println("ID=" + usr.getId() + " | " + usr.getFullname() + " | type=" + usr.getUserType())
        );

        System.out.println("\nADMIN DELETES A USER");
        boolean removed = admin.grantAccess(db, u2.getId(), UserType.CLIENT);
        System.out.println("Deleted Teller? " + removed);

        db.saveFiles();
        System.out.println("\nFINAL DATABASE USER LIST:");
        Arrays.stream(db.getClientsAndTellers(admin)).forEach(usr ->
                System.out.println("ID=" + usr.getId() + " | username=" + usr.getUsername())
        );

        System.out.println("\nIntegration Test 5 COMPLETE");
    }
}
