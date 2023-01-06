package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class UserController {

    private final UserDao userDao;

    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    //endpoint /users (is the base endpoint for this call)
    @GetMapping("/users")
    public String list(Model model) {
//--TODO Exclude current user
        model.addAttribute("users", userDao.findTransferList());
        return "users";
    }

    // endpoint is /users/"name?username="  Finds users id by their name
    @GetMapping("/users/name")
    public int findIdByUsername(String username) {
        return userDao.findIdByUsername(username);
    }

    //endpoint is /users/account?id="
    @GetMapping("users/account")
    public User findUserByaccountid(int id) throws UserNotFoundException {
        return userDao.findUserByAccountId(id);
    }

}
