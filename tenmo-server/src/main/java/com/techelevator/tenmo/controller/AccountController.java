package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.JdbcUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
@Controller
public class AccountController {

    @Autowired
    private final JdbcUserDao dao;

    @Autowired
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService, JdbcUserDao dao) {
        this.dao = dao;
        this.accountService = accountService;
    }

    /**
     * Gets the account balance for the authenticated user.
     * @param model the model to add the attributes to
     * @param principal the authentication user's principal
     * @return the name of the template to render
     * @throws UserNotActivatedException if the authenticated user is not activated
     */
    @GetMapping("/balance")
    public String getAccount(Model model, @AuthenticationPrincipal Principal principal) {
        User user = dao.findByUsername(principal.getName());

        if (!user.isActivated()) {
            throw new UserNotActivatedException("Not Authorized");
        }

        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
        Account account = accountService.findAccountById(accountId);
        BigDecimal balance = account.getBalance();

        model.addAttribute("balance", balance);
        model.addAttribute("accountid", accountId);
        model.addAttribute("user", user);

        return "balance";
    }


    @GetMapping("/accounts/account")
    public int getAccountIdByUserId(@RequestParam int id) {
        return accountService.findAccountIdByUserId(id);


    }
}
