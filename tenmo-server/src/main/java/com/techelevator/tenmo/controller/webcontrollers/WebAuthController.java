package com.techelevator.tenmo.controller.webcontrollers;


import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.JdbcUserDao;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Controller
public class WebAuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;
    private AccountService accountService;
    private TransferService transferService;
    private JdbcUserDao jdbcUserDao;

    public WebAuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                             UserDao userDao, AccountService accountService, TransferService transferService, JdbcUserDao jdbcUserDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.accountService = accountService;
        this.transferService = transferService;
        this.jdbcUserDao = jdbcUserDao;
    }

    @RequestMapping(value = "/weblogin", method = RequestMethod.POST)
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
        List<User>userList = jdbcUserDao.findTransferList(user.getUsername());

        model.addAttribute("Users", userList);
        model.addAttribute("user", user);
        model.addAttribute("loginresponse", loginResponse);
        model.addAttribute("account", account);
        model.addAttribute("transfer", transferList);
        return "authuser";
    }
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public void register(@Valid @RequestBody RegisterUserDTO newUser) {
//        if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
//        }
//    }

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
