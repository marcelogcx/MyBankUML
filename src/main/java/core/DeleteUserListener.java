package core;

import bank.User;

public interface DeleteUserListener {
    void onUserDeletion(User[] users);
}
