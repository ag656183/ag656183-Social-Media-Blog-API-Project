package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.Optional;


public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }


    // Register new account
    public Optional<Account> register(Account account) {
        // Verify username is not null or empty, and password is not null and > 4
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
                return Optional.empty();
        }

        // Check if username is already taken
        if(accountDAO.getAccountByUsername(account.getUsername()).isPresent()) {
            return Optional.empty();
        }

        Account createdAccount = accountDAO.createAccount(account);
        return createdAccount != null ? Optional.of(createdAccount) : Optional.empty();
    }


    // Authenticate user by checking if username and password match a stored account
    public Optional<Account> login(String username, String password) {
        Optional<Account> existingAccount = accountDAO.getAccountByUsername(username);

        if(existingAccount.isPresent() && existingAccount.get().getPassword().equals(password)) {
            return existingAccount;
        }

        return Optional.empty();
    }
}
