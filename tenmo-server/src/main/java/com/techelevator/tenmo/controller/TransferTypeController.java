package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("transfertype")
public class TransferTypeController {

   private TransferTypeService service;

   @Autowired
    public TransferTypeController(TransferTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransferType> findAllTransferType(){
       return service.findAll();
    }

}
