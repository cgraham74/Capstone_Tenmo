package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    List<User> findTransferList();

    User findById(int to);

    User findUserByAccountid(int id);

}
