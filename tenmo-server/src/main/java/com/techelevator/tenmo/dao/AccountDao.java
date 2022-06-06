package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import java.sql.SQLException;


public interface AccountDao {

    Account findBalanceByUserId(int userId) throws SQLException;
}
