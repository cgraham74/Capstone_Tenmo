package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferTypeRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    private AccountService accountService;

    @Autowired
    public TransferController(TransferService transferService, UserDao userDao, AccountRepository accountRepository, TransferTypeRepository transferTypeRepository, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountRepository = accountRepository;
        this.transferTypeRepository = transferTypeRepository;
        this.accountService = accountService;
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
    public void create(@RequestBody Transfer transfer){
        BigDecimal moneyToSend = transfer.getAmount();
        Account accountOfCurrentUser = accountService.findAccountByuserid(transfer.getAccountfrom());
        Account accountofTargetuser = accountService.findAccountByuserid(transfer.getAccountto());

        //Updates the balances with transfer transaction
        if (moneyToSend.compareTo(accountOfCurrentUser.getBalance()) <= 0 && moneyToSend.compareTo(BigDecimal.ZERO) > 0){
            accountOfCurrentUser.setBalance(accountOfCurrentUser.getBalance().subtract(moneyToSend));
            accountofTargetuser.setBalance(accountofTargetuser.getBalance().add(moneyToSend));
            accountService.transferBalance(accountOfCurrentUser, accountofTargetuser);
            transferService.save(transfer);
        }

    }

//    @GetMapping("what")
//    public String getWhat(){
//        return "what";
//    }
}
