package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("transfer/")
public class TransferController {

    @Autowired
    TransferService transferService;

    @PostMapping("transferbalance")
    public Transfer createnewtransfer(int transfertypeid, int transferstatusid, int accountfrom, int accountto, BigDecimal amount){
        return transferService.saveorupdatetransfer(transfertypeid, transferstatusid,accountfrom,accountto,amount);
    }


}
