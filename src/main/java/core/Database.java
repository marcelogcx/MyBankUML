package core;

import java.io.*;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import bank.Admin;
import bank.BankAccount;
import bank.BankAccountType;
import bank.BankOperation;
import bank.Client;
import bank.Deposit;
import bank.Teller;
import bank.Transfer;
import bank.User;
import bank.UserType;
import bank.Withdrawal;

public class Database {
    private final String USER_FILE = "/user.json";
    private final String BANK_ACCOUNT_FILE = "/bankAccount.json";
    private final String BANK_OPERATION_FILE = "/bankOperation.json";
    private Map<String, User> users;
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
            File baseDir = new File(System.getProperty("user.dir"));
            File dataDir = new File(baseDir, "data");

            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                if (created)
                    System.out.println("Created new data directory at: " + dataDir.getAbsolutePath());
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
                Map<String, Object> adminData = new java.util.HashMap<>();
                adminData.put("id", "123");
                adminData.put("userType", "ADMIN");
                adminData.put("fullname", "main admin");
                adminData.put("email", "admin@admin.com");
                adminData.put("username", "admin");
                adminData.put("password", "123");
                adminData.put("isUserBlocked", false);

                Map<String, Object> initialUsersMap = new java.util.HashMap<>();
                initialUsersMap.put("123", adminData);

                mapper.writerWithDefaultPrettyPrinter().writeValue(usersFile, initialUsersMap);

                iStreamUsers = new FileInputStream(usersFile);
                Map<String, User> tempUsers = mapper.readValue(iStreamUsers, new TypeReference<Map<String, User>>() {
                });

                for (Map.Entry<String, User> entry : tempUsers.entrySet()) {
                    String id = entry.getKey();
                    User u = entry.getValue();

                    usedIds.add(id);
                    usernames.put(u.getUsername(), id);

                    users.put(id, u);
                }

                idGen = new IdGenerator(usedIds);
                return;
            }

            Map<String, User> tempUsers = mapper.readValue(iStreamUsers, new TypeReference<Map<String, User>>() {
            });

            for (Map.Entry<String, User> entry : tempUsers.entrySet()) {
                String id = entry.getKey();
                User u = entry.getValue();

                usedIds.add(id);
                usernames.put(u.getUsername(), id);

                users.put(id, u);
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public <T> T writeRecord(Class<T> c, String[] recordData) {
        if (c == Client.class) {
            // Validate username and password
            String username = recordData[2];
            String password = recordData[3];
            String email = recordData[1];

            if (!ValidationUtil.isValidEmail(email)) {
                throw new IllegalArgumentException(ValidationUtil.getEmailErrorMessage(email));
            }
            if (!ValidationUtil.isValidUsername(username)) {
                throw new IllegalArgumentException(ValidationUtil.getUsernameErrorMessage(username));
            }
            if (!ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException(ValidationUtil.getPasswordErrorMessage(password));
            }

            String id = idGen.generateClientId();
            Client tempClient = new Client(id, UserType.CLIENT, recordData[0], recordData[1],
                    recordData[2],
                    recordData[3], new ArrayList<String>(), false);
            users.put(id, tempClient);
            usernames.put(tempClient.getUsername(), id);
            return c.cast(tempClient);
        }
        if (c == Teller.class) {
            // Validate username and password
            String username = recordData[2];
            String password = recordData[3];
            String email = recordData[1];

            if (!ValidationUtil.isValidEmail(email)) {
                throw new IllegalArgumentException(ValidationUtil.getEmailErrorMessage(email));
            }
            if (!ValidationUtil.isValidUsername(username)) {
                throw new IllegalArgumentException(ValidationUtil.getUsernameErrorMessage(username));
            }
            if (!ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException(ValidationUtil.getPasswordErrorMessage(password));
            }

            String id = idGen.generateClientId();
            Teller tempTeller = new Teller(id, UserType.TELLER, recordData[0], recordData[1],
                    recordData[2], recordData[3], false);
            users.put(id, tempTeller);
            usernames.put(tempTeller.getUsername(), id);
            return c.cast(tempTeller);
        }
        if (c == Admin.class) {
            // Validate username and password
            String username = recordData[2];
            String password = recordData[3];
            String email = recordData[1];

            if (!ValidationUtil.isValidEmail(email)) {
                throw new IllegalArgumentException(ValidationUtil.getEmailErrorMessage(email));
            }
            if (!ValidationUtil.isValidUsername(username)) {
                throw new IllegalArgumentException(ValidationUtil.getUsernameErrorMessage(username));
            }
            if (!ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException(ValidationUtil.getPasswordErrorMessage(password));
            }

            String id = idGen.generateClientId();
            Admin tempAdmin = new Admin(id, UserType.ADMIN, recordData[0], recordData[1],
                    recordData[2], recordData[3], false);
            users.put(id, tempAdmin);
            usernames.put(tempAdmin.getUsername(), id);
            return c.cast(tempAdmin);
        } else if (c == BankAccount.class) {
            String id;
            switch (BankAccountType.valueOf(recordData[1])) {
                case CHECKING:
                    id = idGen.generateCheckingAccountId();
                    break;
                case SAVINGS:
                    id = idGen.generateSavingAccountId();
                    break;
                case BUSINESS:
                    id = idGen.generateBusinessAccountId();
                    break;
                default:
                    id = idGen.generateCheckingAccountId();
            }
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
        if (c == User.class) {
            User tempUser = users.get(id);
            return c.cast(tempUser);
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

    public <T> T deleteRecord(Class<T> c, String id) {
        if (c == User.class) {
            User tempUser = readRecord(User.class, id);
            usernames.remove(tempUser.getUsername());
            users.remove(id);
            return c.cast(tempUser);
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

    public List<BankAccount> getClientBankAccounts(List<String> bankAccountIds) {
        List<BankAccount> tempBankAccounts = new ArrayList<>();
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

    public User[] getClients(User u) {
        if (u.getUserType() == UserType.CLIENT) {
            System.err.println("You are not allowed to get clients.");
            return null;
        }
        return users.values().stream().filter(user -> user.getUserType() == UserType.CLIENT)
                .toArray(User[]::new);
    }

    public User[] getClientsAndTellers(User u) {
        if (u.getUserType() != UserType.ADMIN) {
            System.err.println("You are not allowed to get clients.");
            return null;
        }
        return users.values().stream()
                .filter(user -> user.getUserType() == UserType.CLIENT || user.getUserType() == UserType.TELLER)
                .toArray(User[]::new);
    }

    public void saveFiles() {

        ObjectMapper mapper = new ObjectMapper();
        try {
            File baseDir = new File(System.getProperty("user.dir"));
            File dataDir = new File(baseDir, "data");

            if (!dataDir.exists()) {
                boolean created = dataDir.mkdirs();
                if (created)
                    System.out.println("Created new data directory at: " + dataDir.getAbsolutePath());
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

        } catch (IOException e) {
            System.err.println("Failed to save files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Closes files safely
     * Note: Since we're using ObjectMapper which handles file streams
     * automatically,
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
     * 
     * @param c          Class type of the record
     * @param id         ID of the record to update
     * @param recordData Updated data for the record
     * @return Updated record or null if not found
     */
    public <T> T updateRecord(Class<T> c, String id, String[] recordData) {
        if (c == User.class) {
            User tempClient = users.get(id);
            if (tempClient == null) {
                System.err.println("Client not found for update: " + id);
                return null;
            }

            // Validate username and password
            String username = recordData[2];
            String password = recordData[3];
            String email = recordData[1];

            if (!ValidationUtil.isValidEmail(email)) {
                throw new IllegalArgumentException(ValidationUtil.getEmailErrorMessage(email));
            }
            if (!ValidationUtil.isValidUsername(username)) {
                throw new IllegalArgumentException(ValidationUtil.getUsernameErrorMessage(username));
            }
            if (!ValidationUtil.isValidPassword(password)) {
                throw new IllegalArgumentException(ValidationUtil.getPasswordErrorMessage(password));
            }

            // Remove old username mapping
            usernames.remove(tempClient.getUsername());

            // Create updated client (keeping existing bank accounts and user type)
            tempClient.setFullname(recordData[0]);
            tempClient.setEmail(recordData[1]);
            tempClient.setUsername(recordData[2]);
            tempClient.setPassword(recordData[3]);

            usernames.put(tempClient.getUsername(), id);
            System.out.println("Client updated: " + id);
            return c.cast(tempClient);

        } else if (c == BankAccount.class) {
            BankAccount tempBankAccount = bankAccounts.get(id);
            if (tempBankAccount == null) {
                System.err.println("Bank account not found for update: " + id);
                return null;
            }

            tempBankAccount.setAccountName(recordData[0]);
            tempBankAccount.setAccountType(BankAccountType.valueOf(recordData[1]));
            tempBankAccount.setBalance(Double.valueOf(recordData[2]));

            bankAccounts.put(id, tempBankAccount);
            System.out.println("Bank account updated: " + id);
            return c.cast(tempBankAccount);

        } else {
            throw new IllegalArgumentException("Update not supported for type: " + c);
        }
    }

    /**
     * Creates backup copies of all data files
     * 
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
