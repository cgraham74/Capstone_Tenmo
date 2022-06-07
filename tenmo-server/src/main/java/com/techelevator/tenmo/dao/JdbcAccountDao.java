package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{
    JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findBalanceByUserId(int userId) throws SQLException {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account.user_id = ? ;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if(rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        throw new SQLException();
    }

    @Override
    public List<Account> findAllRegisteredUsers() {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT * FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()){
            Account account = mapRowToAccount(results);
            accountList.add(account);
        }
        return accountList;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        var account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUser_id(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }
}
