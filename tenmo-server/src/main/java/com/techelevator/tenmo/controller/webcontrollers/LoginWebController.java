package com.techelevator.tenmo.controller.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginWebController {

    // Login form
    @RequestMapping("/weblogin/login")
    public String login() {
        return "login";
    }

    // Login form with error
    @RequestMapping("/weblogin/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login.html";
    }
}
