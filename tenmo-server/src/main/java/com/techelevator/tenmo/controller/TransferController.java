package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferTypeRepository;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("transfer/")
//@PreAuthorize("isAuthenticated()")
public class TransferController {


    private TransferService transferService;
    private UserDao userDao;
    private AccountRepository accountRepository;
    private TransferTypeRepository transferTypeRepository;

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

    @PostMapping("sendmoney")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestParam("to") int to, @RequestParam("amount") @Positive BigDecimal amount, Principal principal){
        User fromUser = userDao.findByUsername(principal.getName());
        User toUser = userDao.findById(to);
        Account fromUserAccount = accountRepository.findByUser(fromUser);
        Account toUserAccount = accountRepository.findByUser(toUser);
        if(fromUserAccount.getBalance().compareTo(amount) < 0){
            throw new RuntimeException("Insufficient funds");
        }
        Transfer transfer = new Transfer();
        transfer.setAmount(amount);
        transfer.setAccountfrom(fromUserAccount.getAccountid());
        transfer.setAccountto(toUserAccount.getAccountid());
        transfer.setTransfertypeid(2);



    }

//    @GetMapping("what")
//    public String getWhat(){
//        return "what";
//    }
}
