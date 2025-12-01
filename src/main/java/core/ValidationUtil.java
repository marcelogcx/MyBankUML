package core;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * Validates that a username is all lowercase
     *
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        // Check if username is all lowercase
        if (!username.equals(username.toLowerCase())) {
            return false;
        }

        return true;
    }

    /**
     * Validates that the email is valid
     *
     * @param username The username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // Check if username contains @gmail.com
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }

    /**
     * Validates that a password has at least 8 characters,
     * 1 uppercase letter, and 1 special character
     *
     * @param password The password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasSpecialChar = false;

        // Define special characters
        String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>?/~`";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (specialChars.indexOf(c) != -1) {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasSpecialChar;
    }

    /**
     * Gets a user-friendly error message for email validation failure
     *
     * @param email The invalid email
     * @return Error message explaining what's wrong
     */
    public static String getEmailErrorMessage(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        
        if (!matcher.matches()) {
            return "Email format is invalid";
        }
        return "Invalid email";
    }
    /**
     * Gets a user-friendly error message for username validation failure
     *
     * @param username The invalid username
     * @return Error message explaining what's wrong
     */
    public static String getUsernameErrorMessage(String username) {
        if (username == null || username.isEmpty()) {
            return "Username cannot be empty";
        }
        if (!username.equals(username.toLowerCase())) {
            return "Username must be all lowercase";
        }
        return "Invalid username";
    }

    /**
     * Gets a user-friendly error message for password validation failure
     *
     * @param password The invalid password
     * @return Error message explaining what's wrong
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        boolean hasUppercase = false;
        boolean hasSpecialChar = false;
        String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>?/~`";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (specialChars.indexOf(c) != -1) {
                hasSpecialChar = true;
            }
        }

        if (!hasUppercase && !hasSpecialChar) {
            return "Password must contain at least 1 uppercase letter and 1 special character";
        }
        if (!hasUppercase) {
            return "Password must contain at least 1 uppercase letter";
        }
        if (!hasSpecialChar) {
            return "Password must contain at least 1 special character";
        }

        return "Invalid password";
    }
}
