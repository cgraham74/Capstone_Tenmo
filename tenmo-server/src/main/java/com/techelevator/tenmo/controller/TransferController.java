package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferTypeRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transfer/")
//@PreAuthorize("hasRole('ROLE_ADMIN') or #username == authentication.principal.username")
@PreAuthorize("isAuthenticated()")
public class TransferController {


    private final TransferService transferService;
    private UserDao userDao;
    private AccountRepository accountRepository;
    private TransferTypeRepository transferTypeRepository;
    private AccountService accountService;

    @Autowired
    public TransferController(TransferService transferService, UserDao userDao, AccountRepository accountRepository, TransferTypeRepository transferTypeRepository, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountRepository = accountRepository;
        this.transferTypeRepository = transferTypeRepository;
        this.accountService = accountService;
    }

    @GetMapping("history")
    public List<Transfer> getHistory(@RequestParam int id){
        return transferService.getAllToAndFromAccount(id);
    }

    @PostMapping("transferbalance")
    public Transfer createnewtransfer(@RequestBody Transfer transfer){
        return transferService.save(transfer);
    }

    @GetMapping("transfers")
    public List<Transfer> getAllTransfers(){
        return transferService.findAll();
    }

//    @GetMapping("transferfrom")
//    public List<Transfer> getAllTransfersaccountfrom(@RequestParam int id){
//        return transferService.findAllByAccountfrom(id);
//    }
//
//    @GetMapping("transferto")
//    public List<Transfer> getAllTransfersaccountto(@RequestParam int id){
//        return transferService.findAllByAccountto(id);
//    }

    //Gets transfers by id
    @GetMapping("transferid")
    public Transfer findById(@RequestParam int id){
        return transferService.findById(id);
    }
//
//    @GetMapping("transferiddesc")
//    public Object findByIdDesc(@RequestParam int id){
//        return transferService.findByIdDesc(id);
//    }

    @PostMapping("transferfunds")
    public Transfer create(@RequestBody Transfer transfer){
      return transferService.transferBalance(transfer);
    }

    @PostMapping("request")
    public Transfer addNewTransferRequest(@RequestBody Transfer transfer){
        return transferService.requestFundsFromUser(transfer);
    }

    @GetMapping("pending")
    public List<Transfer> findByTransferstatus(@RequestParam int accountfrom){
        return transferService.findAllBystatus(accountfrom);
    }

    @GetMapping("pendingbyid")
    public Transfer findByTransferId(@RequestParam int id){
        return transferService.findById(id);
    }

    @PutMapping("update")
    public void updateTransfer(@RequestParam int statusid, @RequestParam int transferid){
        transferService.update(statusid, transferid);
    }
}
