package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.repositories.AccountRepository;

import com.techelevator.tenmo.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@SpringBootTest
public class AccountControllerTest {

    @MockBean
    private AccountRepository repository;

    @Autowired
    private AccountController controller;

    @Autowired
    AccountService service;


    BigDecimal sutBalance = new BigDecimal("50000.00");
    Account newAccount = new Account(4000, 4000, sutBalance);

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
