package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.repositories.AccountRepository;

import com.techelevator.tenmo.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AccountControllerTest {

    @MockBean
    private AccountRepository repository;

    @Autowired
    private AccountController controller;

    @Autowired
    AccountService service;

    @Test
    public void created_repository_not_null(){
        Assertions.assertNotNull(repository);
    }

    @Test
    public void created_controller_not_null(){
        Assertions.assertNotNull(controller);
    }

    @Test
    public void created_service_not_null() {
        Assertions.assertNotNull(service);
    }
}
