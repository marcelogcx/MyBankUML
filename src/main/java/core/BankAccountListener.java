package core;

public interface BankAccountListener {
    default void onOperation(double balance) {
    };
}
