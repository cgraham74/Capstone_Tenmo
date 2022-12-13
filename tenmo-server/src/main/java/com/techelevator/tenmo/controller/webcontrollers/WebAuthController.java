package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin
@Controller
public class WebAuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;
    private AccountService accountService;
    private TransferService transferService;


    public WebAuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                             UserDao userDao, AccountService accountService, TransferService transferService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.accountService = accountService;
        this.transferService = transferService;

    }
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String login(@Valid LoginDTO loginDto, Model model) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        User user = userDao.findByUsername(loginDto.getUsername());
        WebAuthController.LoginResponse loginResponse =  new WebAuthController.LoginResponse(jwt, user);
        Account account = accountService.findAccountByuserid(Math.toIntExact(user.getId()));
        List<Transfer> transferList = transferService.getAllToAndFromAccount(account.getAccountid());
        List<Transfer> pendingList = transferService.findAllBystatus(account.getAccountid());

        model.addAttribute("pendingtransfers", pendingList);
        model.addAttribute("users", user);
        model.addAttribute("loginresponse", loginResponse);
        model.addAttribute("accounts", account);
        model.addAttribute("transfers", transferList);
        return "main";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String register(@Valid @RequestBody RegisterUserDTO newUser) {
        if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
        }

        return "index";
    }


    @PostMapping("/approve")
    public String approveTransfer(Transfer transfer, Model model) {
        transferService.transferBalance(transfer);
        transferService.update(transfer.getTransferstatusid(), transfer.getId());

        User user = userDao.findUserByAccountid(transfer.getAccountfrom());
        Account account = accountService.findAccountByuserid(Math.toIntExact(user.getId()));
        List<Transfer> transferList = transferService.getAllToAndFromAccount(account.getAccountid());
        List<Transfer> pendingList = transferService.findAllBystatus(account.getAccountid());

        model.addAttribute("pendingtransfers", pendingList);
        model.addAttribute("users", user);
        model.addAttribute("accounts", account);
        model.addAttribute("transfers", transferList);

        return "authuser";
        //  return "authusernew";
    }

    @PostMapping("/reject")
    public String rejectTransfer(Transfer transfer, Model model) {
        transferService.update(transfer.getTransferstatusid(), transfer.getId());

        User user = userDao.findUserByAccountid(transfer.getAccountfrom());
        Account account = accountService.findAccountByuserid(Math.toIntExact(user.getId()));
        List<Transfer> transferList = transferService.getAllToAndFromAccount(account.getAccountid());
        List<Transfer> pendingList = transferService.findAllBystatus(account.getAccountid());

        model.addAttribute("pendingtransfers", pendingList);
        model.addAttribute("users", user);
        model.addAttribute("accounts", account);
        model.addAttribute("transfers", transferList);

      //  return "authusernew";
          return "authuser";
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class LoginResponse {

        private String token;
        private User user;

        LoginResponse(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        void setToken(String token) {
            this.token = token;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

}
