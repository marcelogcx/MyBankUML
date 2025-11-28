package core;

import java.security.SecureRandom;
import java.util.Set;

public class IdGenerator {
    private static final SecureRandom random = new SecureRandom();
    private final Set<String> usedIds;

    public IdGenerator(Set<String> usedIds) {
        this.usedIds = usedIds;
    }

    public String generateClientId() {
        long number = Math.abs(random.nextLong()) % 100_000_000L;
        String id = String.format("%s%08d", "C", number);
        return id;
    }

    public String generateCheckingAccountId() {
        long number = Math.abs(random.nextLong()) % 100_000L;
        String id = String.format("%s%05d", "CHK", number);
        return id;
    }

    public String generateSavingAccountId() {
        long number = Math.abs(random.nextLong()) % 100_000L;
        String id = String.format("%s%05d", "SVN", number);
        return id;
    }

    public String generateBusinessAccountId() {
        long number = Math.abs(random.nextLong()) % 100_000L;
        String id = String.format("%s%05d", "BSN", number);
        return id;
    }

    public String generateOperationId() {
        long number = Math.abs(random.nextLong()) % 1_000_000L;
        String id = String.format("%s%06d", "O", number);
        return id;
    }
}
