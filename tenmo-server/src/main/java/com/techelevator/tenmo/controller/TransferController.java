package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("transfer/")
public class TransferController {


    private TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;

    }

    @PostMapping("transferbalance")
    public Transfer createnewtransfer(@RequestBody Transfer transfer){

        return transferService.save(transfer);
    }

    @GetMapping("transfers")
    public List<Transfer> getAllTransfers(){
        return transferService.findAll();
    }

    @GetMapping("transferfrom")
    public List<Transfer> getAllTransfersaccountfrom(@RequestParam int id){
        return transferService.findAllByAccountfrom(id);
    }

    @GetMapping("transferto")
    public List<Transfer> getAllTransfersaccountto(@RequestParam int id){
        return transferService.findAllByAccountto(id);
    }

    @GetMapping("transferId")
    public Transfer getById(@RequestParam int id){
        return transferService.findById(id);
    }

    @GetMapping("what")
    public String getWhat(){
        return "what";
    }
}
