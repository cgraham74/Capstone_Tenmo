package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransfer;
import com.techelevator.tenmo.dao.JdbcTransferStatus;
import com.techelevator.tenmo.dao.JdbcTransferType;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;

@RestController
public class TransferController {

    @Autowired
    private JdbcTransfer jdbcTransfer;

    @Autowired
    private JdbcTransferType jdbcTransferType;

    @Autowired
    private JdbcTransferStatus jdbcTransferStatus;

//    @Autowired
//    private LoginDTO loginDTO;

    //end point for transfer
  //  @PutMapping("transfer")


    @GetMapping("transfer")
    public Transfer transferBalances(BigDecimal balance, User sender, User receiver) throws SQLException {
        return jdbcTransfer.transferBalanceFromUserToUser(balance, sender, receiver);
    }
  //  }
}
