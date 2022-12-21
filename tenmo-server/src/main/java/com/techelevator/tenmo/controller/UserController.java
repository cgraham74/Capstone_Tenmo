package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final UserDao dao;

    @Autowired
    public UserController(UserDao userDao) {
        this.dao = userDao;
    }

    //endpoint /users (is the base endpoint for this call)
    @GetMapping("/users")
    public String list(Model model) {
//--TODO Exclude current user
        model.addAttribute("users", dao.findTransferList());
        return "users";
    }

    // endpoint is /users/"name?username="  Finds users id by their name
    @GetMapping("/users/name")
    public int findIdByUsername(String username) {
        return dao.findIdByUsername(username);
    }

    // endpoint is /users/recipients?username="
    @GetMapping("users/list")
    public List<User> getAllAvailableRecipients() {
        return dao.findTransferList();
    }

    //endpoint is /users/account?id="
    @GetMapping("users/account")
    public User findUserByaccountid(int id) {
        return dao.findUserByAccountid(id);
    }

}
