package com.techelevator.tenmo.controller.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@CrossOrigin
public class LoginController {
    @GetMapping("/")
    public String login(Model model){
        model.addAttribute("message", "Log in");
        System.out.println("Login Controller Invoked");
        return "login";
    }
}
