package userManager;

import BankSystem.BankSystem;
import BankSystem.SystemImplement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class    UserManager {

    private final Set<String> usersSet;
    private BankSystem bankEngine;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public void setBankEngine(BankSystem bankEngine) {
        this.bankEngine = bankEngine;
    }

    public synchronized void addUser(String username) {
        usersSet.add(username);
        bankEngine.addCustomerToBank(username);
    }

    public synchronized void removeUser(String username) {
        usersSet.remove(username);
        //TODO not sure if we need to remove him from customers list in bank
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        return usersSet.contains(username);
    }
}
