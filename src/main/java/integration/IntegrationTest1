package integration;

import bank.*;
import core.Database;

public class IntegrationTest1 {

    public static void main(String[] args) {
        System.out.println("Integration Test 1: To test the individual low-level modules (Deposit, Withdrawal, and Transaction).");
        Database db = new Database();

        // Create test account
        String[] recordData = {
                "Test Checking",
                BankAccountType.CHECKING.name(),
                "1000.0"
        };
        BankAccount account = db.writeRecord(BankAccount.class, recordData);
        account.setDatabase(db);

        System.out.println("Initial balance: " + account.getBalance());

        // Test Deposit 
        String[] depositData = {
                "Test deposit",
                account.getId(),
                "200.0",
                java.time.LocalDate.now().toString(),
                "false"
        };
        Deposit deposit = db.writeRecord(Deposit.class, depositData);
        deposit.setDatabase(db);
        deposit.executeOperation();
        deposit.record();
        System.out.println("After deposit: " +
                db.readRecord(BankAccount.class, account.getId()).getBalance());

        // Test Withdrawal 
        String[] withdrawalData = {
                "Test withdrawal",
                account.getId(),
                "150.0",
                java.time.LocalDate.now().toString(),
                "false"
        };
        Withdrawal withdrawal = db.writeRecord(Withdrawal.class, withdrawalData);
        withdrawal.setDatabase(db);
        withdrawal.executeOperation();
        withdrawal.record();
        System.out.println("After withdrawal: " +
                db.readRecord(BankAccount.class, account.getId()).getBalance());

        // Test Transfer 
        // create destination account
        BankAccount dest = db.writeRecord(BankAccount.class, new String[]{
                "Dest Account",
                BankAccountType.CHECKING.name(),
                "300.0"
        });
        dest.setDatabase(db);

        String[] transferData = {
                "Test transfer",
                account.getId(),
                dest.getId(),
                "250.0",
                java.time.LocalDate.now().toString(),
                "false"
        };
        Transfer transfer = db.writeRecord(Transfer.class, transferData);
        transfer.setDatabase(db);
        transfer.executeOperation();
        transfer.record();

        BankAccount srcAfter = db.readRecord(BankAccount.class, account.getId());
        BankAccount destAfter = db.readRecord(BankAccount.class, dest.getId());

        System.out.println("After transfer (source): " + srcAfter.getBalance());
        System.out.println("After transfer (destintation): " + destAfter.getBalance());

    }
}
