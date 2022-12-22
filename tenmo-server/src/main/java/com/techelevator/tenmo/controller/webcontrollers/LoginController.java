//package com.techelevator.tenmo.controller.webcontrollers;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@PreAuthorize("permitAll()")
//@Controller
//@CrossOrigin
//public class LoginController {
//    @GetMapping("/")
//    public String showLogInForm(Model model){
//        model.addAttribute("message", "Log in");
//        return "login";
//    }
//
//    @GetMapping("/newuser")
//    public String newUserForm(Model model){
//        model.addAttribute("message", "New User Registration");
//        return "newuser";
//    }
//}
