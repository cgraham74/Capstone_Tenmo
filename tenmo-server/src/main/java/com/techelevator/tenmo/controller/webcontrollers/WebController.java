package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
@CrossOrigin
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

    @GetMapping("/log")
    public String getLoggedInUser(Model model){
        System.out.println("WEBController model at /log : " + model);
        model.addAttribute("message", "Login Page");
        System.out.println("WEBController model at /log LOG model assigned : " + model);
        return "index";
    }

}
