package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class UserAccountController {

    @Autowired
    private JdbcUserDao jdbcUserDao;
    @Autowired
    private JdbcAccountDao jdbcAccountDao;

    @GetMapping("balance")
    public Account getUserBalance(int id) throws SQLException {
        return jdbcAccountDao.findBalanceByUserId(id);
    }
}
