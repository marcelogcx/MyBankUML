package core;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.BankAccount;
import bank.BankAccountType;
import bank.BankOperation;
import bank.Client;
import bank.Deposit;
import bank.Transfer;
import bank.UserType;
import bank.Withdrawal;

public class Database {
    private final String USER_FILE = "/user.json";
    private final String BANK_ACCOUNT_FILE = "/bankAccount.json";
    private final String BANK_OPERATION_FILE = "/bankOperation.json";
    private Map<String, Client> users;
    private Map<String, String> usernames;
    private Map<String, BankAccount> bankAccounts;
    private Map<String, BankOperation> bankOperations;
    private IdGenerator idGen;
    private Set<String> usedIds;

    public Database() {
        idGen = new IdGenerator(usedIds);
        usedIds = new HashSet<>();
        users = new HashMap<>();
        usernames = new HashMap<>();
        bankAccounts = new HashMap<>();
        bankOperations = new HashMap<>();
        openFiles();
    }

    public void openFiles() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Determine base directory (JAR folder or classes folder)
            File jarDir = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File baseDir = jarDir.isFile() ? jarDir.getParentFile() : jarDir;

            // Create data folder inside base directory
            File dataDir = new File(baseDir, "data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            // External files to read if exist
            File usersFile = new File(dataDir, USER_FILE);
            File bankAccountsFile = new File(dataDir, BANK_ACCOUNT_FILE);
            File bankOperationsFile = new File(dataDir, BANK_OPERATION_FILE);

            // Load Users
            InputStream iStreamUsers = usersFile.exists() ? new FileInputStream(usersFile)
                    : getClass().getClassLoader().getResourceAsStream(USER_FILE);

            if (iStreamUsers == null) {
                System.err.println("Users JSON file not found.");
                return;
            }
            users = mapper.readValue(iStreamUsers, new TypeReference<Map<String, Client>>() {
            });
            for (String userId : users.keySet()) {
                usedIds.add(userId);
                usernames.put(users.get(userId).getUsername(), userId);
            }
            idGen = new IdGenerator(usedIds);

            // Load Bank Accounts
            InputStream iStreamBankAccounts = bankAccountsFile.exists()
                    ? new FileInputStream(bankAccountsFile)
                    : getClass().getClassLoader().getResourceAsStream(BANK_ACCOUNT_FILE);
            if (iStreamBankAccounts == null) {
                System.err.println("Bank Accounts JSON file not found.");
                return;
            }
            bankAccounts = mapper.readValue(iStreamBankAccounts,
                    new TypeReference<Map<String, BankAccount>>() {
                    });
            /*
             * for (String bankAccountId : bankAccounts.keySet()) {
             * usedIds.add(bankAccountId);
             * }
             */

            // Load Bank Operations
            InputStream iStreamBankOperations = bankOperationsFile.exists()
                    ? new FileInputStream(bankOperationsFile)
                    : getClass().getClassLoader().getResourceAsStream(BANK_OPERATION_FILE);
            if (iStreamBankOperations == null) {
                System.err.println("Bank Operations JSON file not found.");
                return;
            }
            bankOperations = mapper.readValue(iStreamBankOperations,
                    new TypeReference<Map<String, BankOperation>>() {
                    });

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public <T> T writeRecord(Class<T> c, String[] recordData) {
        if (c == Client.class) {
            String id = idGen.generateClientId();
            Client tempClient = new Client(id, UserType.CLIENT, recordData[0], recordData[1],
                    recordData[2],
                    recordData[3], new ArrayList<String>());
            users.put(id, tempClient);
            usernames.put(tempClient.getUsername(), id);
            return c.cast(tempClient);
        } else if (c == BankAccount.class) {
            String id = idGen.generateBankAccountId();
            BankAccount bankAccountTemp = new BankAccount(id, recordData[0], BankAccountType.valueOf(recordData[1]),
                    Double.valueOf(recordData[2]), new ArrayList<String>());
            bankAccounts.put(id, bankAccountTemp);
            return c.cast(bankAccountTemp);
        } else if (c == Deposit.class) {
            String id = idGen.generateOperationId();
            Deposit depositTemp = new Deposit(id, recordData[0], recordData[1], Double.valueOf(recordData[2]),
                    recordData[3], Boolean.parseBoolean(recordData[4]));
            bankOperations.put(id, depositTemp);
            return c.cast(depositTemp);
        } else if (c == Withdrawal.class) {
            String id = idGen.generateOperationId();
            Withdrawal withdrawalTemp = new Withdrawal(id, recordData[0], recordData[1], Double.valueOf(recordData[2]),
                    recordData[3], Boolean.parseBoolean(recordData[4]));
            bankOperations.put(id, withdrawalTemp);
            return c.cast(withdrawalTemp);
        } else if (c == Transfer.class) {
            String id = idGen.generateOperationId();
            Transfer transferTemp = new Transfer(id, recordData[0], recordData[1], recordData[2],
                    Double.valueOf(recordData[3]),
                    recordData[4], Boolean.parseBoolean(recordData[5]));
            bankOperations.put(id, transferTemp);
            return c.cast(transferTemp);
        } else {
            throw new IllegalArgumentException("Unsupported Type: " + c);
        }

    }

    public <T> T readRecord(Class<T> c, String id) {
        if (c == Client.class) {
            Client tempClient = users.get(id);
            return c.cast(tempClient);
        } else if (c == BankAccount.class) {
            BankAccount tempBankAccount = bankAccounts.get(id);
            return c.cast(tempBankAccount);
        } else if (c == BankOperation.class) {
            BankOperation tempBankOperation = bankOperations.get(id);
            return c.cast(tempBankOperation);
        } else {
            throw new IllegalArgumentException("Unsupported Type: " + c);
        }
    }

    public void deleteRecord() {

    }

    public boolean usernameExists(String username) {
        return usernames.get(username) == null ? false : true;
    }

    public String getIdFromUsername(String username) {
        return usernames.get(username);
    }

    public ArrayList<BankAccount> getClientBankAccounts(ArrayList<String> bankAccountIds) {
        ArrayList<BankAccount> tempBankAccounts = new ArrayList<>();
        for (String id : bankAccountIds) {
            tempBankAccounts.add(bankAccounts.get(id));
        }
        return tempBankAccounts;
    }

    public ArrayList<BankOperation> getBankAccountOperations(ArrayList<String> bankAccountOperationIds) {
        ArrayList<BankOperation> tempBankOperations = new ArrayList<>();
        for (String id : bankAccountOperationIds) {
            tempBankOperations.add(bankOperations.get(id));
        }
        return tempBankOperations;
    }

    public void saveFiles() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File baseDir = jarFile.isFile() ? jarFile.getParentFile() : jarFile;

            // Create data folder inside base directory
            File dataDir = new File(baseDir, "data");
            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                if (!created) {
                    System.err.println("Failed to create data directory at " + dataDir.getAbsolutePath());
                    return; // early exit if data folder cannot be created
                }
            }
            // Build full file paths inside data folder
            File usersFile = new File(dataDir, USER_FILE);
            File bankAccountsFile = new File(dataDir, BANK_ACCOUNT_FILE);
            File bankOperationsFile = new File(dataDir, BANK_OPERATION_FILE);
            if (users != null) {
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(usersFile, users);
            }

            if (bankAccounts != null) {
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(bankAccountsFile, bankAccounts);
            }

            if (bankOperations != null) {
                mapper.writerWithDefaultPrettyPrinter()
                        .writeValue(bankOperationsFile, bankOperations);
            }

            System.out.println("All maps saved successfully.");

        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to save files: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
