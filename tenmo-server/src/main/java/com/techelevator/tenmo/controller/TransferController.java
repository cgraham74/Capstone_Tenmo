package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.JdbcUserDao;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@PreAuthorize("isAuthenticated()")
@Controller
@Data
public class TransferController {


    private final TransferService transferService;
    private UserDao userDao;
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;
    private AccountService accountService;
    private JdbcUserDao dao;



    @Autowired
    public TransferController(TransferService transferService, UserDao userDao, AccountRepository accountRepository, TransferRepository transferRepository, AccountService accountService, JdbcUserDao dao) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.accountService = accountService;
        this.dao = dao;
    }
    //Will need to find the syntax to get a parameter {id} in Thymeleaf
    @GetMapping({"/transfers/records"})
    public ModelAndView getRecords(HttpServletRequest request, Model model) {
       ModelAndView mav = new ModelAndView("history");
       List<Transfer> records = transferRepository.findAll();
       mav.addObject("transfers", records);
        return mav;
    }

    @GetMapping("/transfers/transfer")
    public Transfer findTransferById(@RequestParam int id) {
        return transferService.findById(id);
    }

    @PostMapping("/transfers/sendmoney")
    public Transfer create(@RequestBody Transfer transfer) {
        return transferService.transferBalance(transfer);
    }

    @PostMapping("/transfers/request")
    public Transfer addNewTransferRequest(@RequestBody Transfer transfer) {
        return transferService.requestFundsFromUser(transfer);
    }

    @GetMapping("/transfers/pending")
    public List<Transfer> findByTransferstatus(@RequestParam int accountfrom) {
        return transferService.findAllBystatus(accountfrom);
    }

    @PutMapping("/transfers/update")
    public void updateTransfer(@RequestParam int statusid, @RequestParam int transferid) {
        transferService.update(statusid, transferid);
    }
}
