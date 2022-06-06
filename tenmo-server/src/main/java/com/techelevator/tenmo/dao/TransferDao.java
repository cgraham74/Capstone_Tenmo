package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;


public interface TransferDao {


    Transfer transferBalanceFromUserToUser(BigDecimal balance, User sender, User receiver) throws SQLException;
}
