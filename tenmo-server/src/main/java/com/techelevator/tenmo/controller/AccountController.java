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

    /**
     * This function takes in a Model, HttpSession, and principal
     * and will check the balance of the current logged in user. It should
     * only allow the balance to be accessed if the current logged in user
     * is the authenticated Principal
     * @param model A Thymeleaf Object used to balance and accountid data
     * @param session An object used to store user state and activity including
     *                authentication information to provide a personalized experience.
     * @param principal An object representing the identity of the of an authenticated
     *                  user to determine resource access.
     * @return The current authenticated user's balance.
     */
    @GetMapping("/balance")
    public String getAccount(Model model, HttpSession session, Principal principal) {
        user = (User) session.getAttribute("user");

        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
        //TODO implement Principal functionality
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
