package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("transferstatus")
public class TransferStatusController {

    //made final so we cannot change our service layer by accident
    private final TransferStatusService service;

    //Dependency injecting service into the transferstatus controller via the constructor. Doesn't need Autowire annotation but using for readability
    @Autowired
    public TransferStatusController(TransferStatusService service) {
        this.service = service;
    }

    // endpoint /transferstatus to display all available statuses
    @GetMapping
    public List<TransferStatus> findAllTransferStatus(){
        return service.findAll();
    }

    @GetMapping("byid")
    public TransferStatus findById(int id) {
        return service.findById(id);
    }
}
