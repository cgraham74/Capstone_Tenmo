package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/")
public class UserController {

    private final UserDao dao;

    @Autowired
    public UserController(UserDao userDao) {
        this.dao = userDao;
    }

    //endpoint /users (is the base endpoint for this call)
    @GetMapping("")
    public List<User> list() {
        return dao.findAll();
    }

    // endpoint is /users/"name?username="  Finds users id by their name
    @GetMapping("name")
    public int findIdByUsername(String username) {
        return dao.findIdByUsername(username);
    }

    // endpoint is /users/recipients?username="
    @GetMapping("list")
    public List<User> getAllAvailableRecipients() {
        return dao.findTransferList();
    }

    //endpoint is /users/account?id="
    @GetMapping("account")
    public User findUserByaccountid(int id) {
        return dao.findUserByAccountid(id);
    }

}
