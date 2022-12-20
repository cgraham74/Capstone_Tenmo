package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.JdbcUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;


//@PreAuthorize("isAuthenticated()")
@CrossOrigin
@Controller
public class AccountController {

    private final JdbcUserDao dao;
    private final AccountService accountService;
    private User user;

    @Autowired
    public AccountController(AccountService accountService, JdbcUserDao dao) {
        this.dao = dao;
        this.accountService = accountService;
    }

    //Will return current user's balance
    @GetMapping("/balance")
    public String getAccount(Model model, HttpSession session, Principal principal) {
        user = (User) session.getAttribute("user");
      //  User user = dao.findById(id);
        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
      //  if (user.getUsername().equals(principal.getName())) {
        Account account = accountService.findAccountById(accountId);
       BigDecimal balance = account.getBalance();
            model.addAttribute("balance", balance);
            model.addAttribute("accountid", accountId);
           // return accountService.findAccountByuserid(accountId);
            return "balance";
       // }
       // throw new UserNotActivatedException("Not Authorized");
    }

    @GetMapping("/accounts/account")
    public int getAccountIdByUserId(@RequestParam int id) {
        return accountService.findAccountIdByUserId(id);
    }
}
