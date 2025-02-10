package Service;

import DAO.AccountDAO;
import Model.Account;



public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerUser(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            return null; // Validation failed
        }

        if (accountDAO.doesUsernameExist(username)) {
            return null; // Username already exists
        }

        return accountDAO.insertAccount(new Account(0, username, password)); // Insert user
    }

    public Account loginUser(String username, String password) {
        if (username == null || username.isBlank() || password == null) {
            return null; // Invalid input
        }
    
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}