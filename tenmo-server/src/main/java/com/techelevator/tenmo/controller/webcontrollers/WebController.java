package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private UserDao userDao;
    private AccountService accountService;

    public WebController(UserDao userDao, AccountService accountService) {
        this.userDao = userDao;
        this.accountService = accountService;
    }

    public WebController() {
    }

    @GetMapping("/weblog")
    public String getLoggedInUser(Model model){
        model.addAttribute("message", "Login Page");
        return "weblogin";
    }

}
