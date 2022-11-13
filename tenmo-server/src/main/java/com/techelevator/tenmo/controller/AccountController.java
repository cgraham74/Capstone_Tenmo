package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.JdbcUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "/accounts/")
public class AccountController {

    private final JdbcUserDao dao;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService, JdbcUserDao dao) {
        this.dao = dao;
        this.accountService = accountService;
    }

    //Will list current user's balance given the User's ID
    @GetMapping("balance")
    public Account getAccount(@RequestParam int id, Principal principal) {
        User user = dao.findById(id);
        if (user.getUsername().equals(principal.getName())) {
            return accountService.findAccountByuserid(id);
        }
        throw new UserNotActivatedException("Not Authorized");
    }

    @GetMapping("account")
    public int getAccountIdByUserId(@RequestParam int id) {
        return accountService.findAccountIdByUserId(id);
    }
}
