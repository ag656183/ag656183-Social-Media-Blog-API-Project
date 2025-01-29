package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Get all accounts
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "Select * FROM account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return accounts;
    }

        // Get accound by ID
        public Account getAccountByID(int id) {
            Connection connection = ConnectionUtil.getConnection();
    
            try {
                String sql = "SELECT * FROM account WHERE account_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, id);
    
                ResultSet rs = preparedStatement.executeQuery();
                while(rs.next()) {
                    Account account = new Account(rs.getInt("account_id"), rs.getString())
                }
    
            }
        }
}
