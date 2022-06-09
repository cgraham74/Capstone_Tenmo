package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDao dao;

    // Dependency injecting UserDao (our repository) at the UserController constructor level.
    @Autowired
    public UserController(UserDao userDao) {
        this.dao = userDao;
    }

    //endpoint /user (is the base endpoint for this call)
    @GetMapping("")
    public List<User> list(){
        return dao.findAll();
    }

    // endpoint is /user/"name?username="
    @GetMapping("/name")
    public int findIdByUsername(String username){
        return dao.findIdByUsername(username);
    }

    // endpoint is /user/"recipients?username="
    @GetMapping("/recipients")
    public List<User> getAllAvailableRecipients(String username){
    return dao.findTransferList(username);
    }
}
