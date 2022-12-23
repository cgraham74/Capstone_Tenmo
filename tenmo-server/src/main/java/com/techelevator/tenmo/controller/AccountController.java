package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.UserNotActivatedException;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.services.JdbcUserDao;
import com.techelevator.tenmo.services.UserDao;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
@Controller
public class AccountController {

    @Autowired
    private final JdbcUserDao dao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private final AccountService accountService;

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
    public String getAccount(@ModelAttribute("user") LoginDTO loginDTO,
                             Model model, HttpSession session, @AuthenticationPrincipal Principal principal) {
        User user = (User) session.getAttribute("user");
        //TODO implement Security.
        User balanceUser = userDao.findByUsername(user.getUsername());
        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));

        Account account = accountService.findAccountById(accountId);
        BigDecimal balance = account.getBalance();
        model.addAttribute("balance", balance);
        model.addAttribute("accountid", accountId);
        model.addAttribute("user",user);

        //   if (user.getUsername().equals(principal.getName())) {

        // return accountService.findAccountByuserid(accountId);


        System.out.println("Value of the Principal : " + principal  );
        return "balance";

    }
    //   }
    //  throw new UserNotActivatedException("Not Authorized");
    @GetMapping("/accounts/account")
    public int getAccountIdByUserId(@RequestParam int id) {
        return accountService.findAccountIdByUserId(id);


    }
}
