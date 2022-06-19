package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "/account/")
public class AccountController {


    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //end point /account/"balancebyid?id=" Will list current user's balance given the User's ID
    @GetMapping("balancebyuserid")
    public Account getAccount(@RequestParam int id) {
        return accountService.findAccountByuserid(id);
    }
}
