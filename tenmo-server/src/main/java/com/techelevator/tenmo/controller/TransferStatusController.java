package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("transferstatus")
public class TransferStatusController {

    //Made final so that we cannot change our service layer.
    private final TransferStatusService service;

    //Dependency injecting service into the transferstatus controller via the constructor. Doesn't need Autowire annotation but using for readability
    @Autowired
    public TransferStatusController(TransferStatusService service) {
        this.service = service;
    }

    //Endpoint /transferstatus to display all available statuses
    @GetMapping
    public List<TransferStatus> findAllTransferStatus(){
        return service.findAll();
    }

    @GetMapping("byid")
    public TransferStatus findById(int id) {
        return service.findById(id);
    }
}
