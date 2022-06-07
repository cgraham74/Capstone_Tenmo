package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "account/")
public class AccountController {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @GetMapping("balancebyid")
   public Account getBalancebyId(@RequestParam int id) {
        return accountService.findAccountById(id);
    }

    @GetMapping("balancebyuserid")
    public Account getTransferUserList(int id) {
        return accountService.findAccountByuserid(id);
    }

}
