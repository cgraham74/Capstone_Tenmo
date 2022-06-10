package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping(path = "account/")
public class AccountController {


    private AccountService accountService;
    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //end point /account/"balancebyid?id=" Will list current user's balance given the account ID
    @GetMapping("balancebyid")
   public Account getBalancebyId(@RequestParam int id) {
        return accountService.findAccountById(id);
    }

    //end point /account/"balancebyid?id=" Will list current user's balance given the User's ID
    @GetMapping("balancebyuserid")
    public Account getTransferUserList(@RequestParam int id) {
        return accountService.findAccountByuserid(id);
    }

    //endpoint account/updatebalance/id?=" updates current user's balance
    @PostMapping("updatebalance/{id}")
    public Account updateBalanceById(@RequestBody Account account){
        return accountService.save(account);
    }

}
