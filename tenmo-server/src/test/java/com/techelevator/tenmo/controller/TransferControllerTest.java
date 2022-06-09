package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.repositories.TransferRepository;
import com.techelevator.tenmo.services.TransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class TransferControllerTest {

    @MockBean
    private
    TransferRepository transferRepository;

    @Autowired
    TransferController transferController;

    @Autowired
    TransferService transferService;

    @Test
    public void created_repository_not_null(){
        Assertions.assertNotNull(transferRepository);
    }

    @Test
    public void created_controller_not_null(){
        Assertions.assertNotNull(transferController);
    }

    @Test
    public void created_service_not_null(){
        Assertions.assertNotNull(transferService);
    }
}
