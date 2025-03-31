package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        if (account == null || account.getUsername() == null || account.getPassword() == null) {
            return null;
        }
        if (account.getUsername().isBlank() || account.getPassword().length() < 4 || accountDAO.findByUsername(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        Account existingAccount = accountDAO.findByUsername(username);
        return (existingAccount != null && existingAccount.getPassword().equals(password)) ? existingAccount : null;
        
    }
}   
