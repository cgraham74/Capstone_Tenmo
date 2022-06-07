package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

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

    @GetMapping("transferlist")
    public List<User> getTransferUserList(String username) {
        return jdbcUserDao.findTransferList(username);
    }

}
