package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@Controller
public class TransferTypeController {

   private final TransferTypeService service;

   @Autowired
    public TransferTypeController(TransferTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<TransferType> findAllTransferType(){
       return service.findAll();
    }

    @GetMapping("/transfertype")
    public TransferType findById(int id) {
       return service.findById(id);
    }

}
