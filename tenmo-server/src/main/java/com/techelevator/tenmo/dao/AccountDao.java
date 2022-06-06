package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import java.sql.SQLException;
import java.util.List;


public interface AccountDao {

    Account findBalanceByUserId(int userId) throws SQLException;

    List<Account> findAllRegisteredUsers();
}
