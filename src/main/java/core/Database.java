package core;

import java.io.*;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.Admin;
import bank.BankAccount;
import bank.BankAccountType;
import bank.BankOperation;
import bank.Client;
import bank.Deposit;
import bank.Teller;
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

    public Client writeUser(UserType userType, String[] recordData) {
        String id = idGen.generateClientId();
        Client tempClient = new Client(id, userType, recordData[0], recordData[1],
                recordData[2], recordData[3], new ArrayList<String>());
        users.put(id, tempClient);
        usernames.put(tempClient.getUsername(), id);
        return tempClient;
    }

    public <T> T writeRecord(Class<T> c, String[] recordData) {
        if (c == Client.class) {
            return c.cast(writeUser(UserType.CLIENT, recordData));
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

    public boolean usernameExists(String username) {
        return usernames.get(username) == null ? false : true;
    }

    public String getIdFromUsername(String username) {
        return usernames.get(username);
    }

    /**
     * Creates an Admin object from a Client with ADMIN userType
     * @param userId ID of the user
     * @return Admin object or null if user is not an admin
     */
    public Admin getAdmin(String userId) {
        Client client = users.get(userId);
        if (client == null || client.getUserType() != UserType.ADMIN) {
            return null;
        }
        Admin admin = new Admin(client.getId(), client.getUsername(), client.getPassword());
        admin.setDatabase(this);
        return admin;
    }

    /**
     * Creates a Teller object from a Client with TELLER userType
     * @param userId ID of the user
     * @return Teller object or null if user is not a teller
     */
    public Teller getTeller(String userId) {
        Client client = users.get(userId);
        if (client == null || client.getUserType() != UserType.TELLER) {
            return null;
        }
        Teller teller = new Teller(client.getId(), client.getUsername(), client.getPassword());
        teller.setDatabase(this);
        return teller;
    }

    /**
     * Gets the user type for a given user ID
     * @param userId ID of the user
     * @return UserType or null if user doesn't exist
     */
    public UserType getUserType(String userId) {
        Client client = users.get(userId);
        return client != null ? client.getUserType() : null;
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

    public ArrayList<Client> getAllUsers() {
        return new ArrayList<>(users.values());
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

    /**
     * Closes files safely
     * Note: Since we're using ObjectMapper which handles file streams automatically,
     * this method performs cleanup operations on the data structures
     */
    public void closeFiles() {
        System.out.println("Closing database and cleaning up resources...");
        // Save any pending changes before closing
        saveFiles();

        // Clear data structures if needed (optional)
        // users.clear();
        // bankAccounts.clear();
        // bankOperations.clear();

        System.out.println("Database closed successfully.");
    }

    /**
     * Updates an existing record in the database
     * @param c Class type of the record
     * @param id ID of the record to update
     * @param recordData Updated data for the record
     * @return Updated record or null if not found
     */
    public <T> T updateRecord(Class<T> c, String id, String[] recordData) {
        if (c == Client.class) {
            Client existingClient = users.get(id);
            if (existingClient == null) {
                System.err.println("Client not found for update: " + id);
                return null;
            }

            // Remove old username mapping
            usernames.remove(existingClient.getUsername());

            // Create updated client (keeping existing bank accounts and user type)
            Client updatedClient = new Client(
                id,
                existingClient.getUserType(),
                recordData[0], // fullname
                recordData[1], // email
                recordData[2], // username
                recordData[3], // password
                existingClient.getBankAccountIds()
            );

            users.put(id, updatedClient);
            usernames.put(updatedClient.getUsername(), id);
            System.out.println("Client updated: " + id);
            return c.cast(updatedClient);

        } else if (c == BankAccount.class) {
            BankAccount existingAccount = bankAccounts.get(id);
            if (existingAccount == null) {
                System.err.println("Bank account not found for update: " + id);
                return null;
            }

            // Update bank account (keeping existing operations)
            BankAccount updatedAccount = new BankAccount(
                id,
                recordData[0], // accountName
                BankAccountType.valueOf(recordData[1]), // accountType
                Double.valueOf(recordData[2]), // balance
                existingAccount.getOperationIds()
            );

            bankAccounts.put(id, updatedAccount);
            System.out.println("Bank account updated: " + id);
            return c.cast(updatedAccount);

        } else {
            throw new IllegalArgumentException("Update not supported for type: " + c);
        }
    }

    /**
     * Removes a record from the database
     * @param c Class type of the record
     * @param id ID of the record to delete
     * @return true if deleted, false if not found
     */
    public <T> boolean deleteRecord(Class<T> c, String id) {
        if (c == Client.class) {
            Client client = users.remove(id);
            if (client != null) {
                usernames.remove(client.getUsername());
                usedIds.remove(id);
                System.out.println("Client deleted: " + id);
                return true;
            }
            System.err.println("Client not found for deletion: " + id);
            return false;

        } else if (c == BankAccount.class) {
            BankAccount account = bankAccounts.remove(id);
            if (account != null) {
                System.out.println("Bank account deleted: " + id);
                return true;
            }
            System.err.println("Bank account not found for deletion: " + id);
            return false;

        } else if (c == BankOperation.class) {
            BankOperation operation = bankOperations.remove(id);
            if (operation != null) {
                System.out.println("Bank operation deleted: " + id);
                return true;
            }
            System.err.println("Bank operation not found for deletion: " + id);
            return false;

        } else {
            throw new IllegalArgumentException("Delete not supported for type: " + c);
        }
    }

    /**
     * Creates backup copies of all data files
     * @return true if backup successful, false otherwise
     */
    public boolean backupDataFiles() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File baseDir = jarFile.isFile() ? jarFile.getParentFile() : jarFile;

            // Create backup folder
            File backupDir = new File(baseDir, "data/backup");
            if (!backupDir.exists()) {
                boolean created = backupDir.mkdirs();
                if (!created) {
                    System.err.println("Failed to create backup directory");
                    return false;
                }
            }

            // Create timestamp for backup files
            String timestamp = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                    .format(java.time.LocalDateTime.now());

            // Backup users file
            File usersBackup = new File(backupDir, "user_backup_" + timestamp + ".json");
            if (users != null) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(usersBackup, users);
            }

            // Backup bank accounts file
            File accountsBackup = new File(backupDir, "bankAccount_backup_" + timestamp + ".json");
            if (bankAccounts != null) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(accountsBackup, bankAccounts);
            }

            // Backup bank operations file
            File operationsBackup = new File(backupDir, "bankOperation_backup_" + timestamp + ".json");
            if (bankOperations != null) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(operationsBackup, bankOperations);
            }

            System.out.println("Backup created successfully at: " + backupDir.getAbsolutePath());
            return true;

        } catch (IOException | URISyntaxException e) {
            System.err.println("Failed to create backup: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
