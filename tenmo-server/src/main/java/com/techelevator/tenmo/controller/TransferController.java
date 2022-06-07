package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("transfer/")
public class TransferController {

    @Autowired
    TransferService transferService;

    TransferType transferType;

    TransferStatus transferStatus;

    @PostMapping("transferbalance")
    public Transfer createnewtransfer(@RequestBody int id, @Valid @RequestBody int transfertypeid, @Valid @RequestBody int transferstatusid, @RequestBody int accountfrom, @RequestBody int accountto, @RequestBody BigDecimal amount){

        return transferService.saveorupdatetransfer(id, transfertypeid, transferstatusid,accountfrom,accountto,amount);
    }

    @GetMapping("transfers")
    public List<Transfer> getAllTransfers(){
        return transferService.findAll();
    }

}
