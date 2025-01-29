package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.Optional;

public class AccountDAO {

    // Create new account
    public Account createAccount(Account account) {
        String sql = " INSERT INTO account(username, password) VALUES(?, ?)";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if(rs.next()) {
                    int accountId = rs.getInt(1);
                    return new Account(accountId, account.getUsername(), account.getPassword());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // Get account by account_id
    public Optional<Account> getAccountByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";

        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            if(rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
