package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import com.techelevator.tenmo.security.SecurityUtils;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;
import java.util.List;

//@PreAuthorize("isAuthenticated()")

@CrossOrigin
@Controller
@Data
public class TransferController {


    private final TransferService transferService;
    private UserDao userDao;
    private AccountService accountService;
    Authentication authentication;


    @Autowired
    public TransferController(TransferService transferService, UserDao userDao, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountService = accountService;

    }
    //Will need to find the syntax to get a parameter {id} in Thymeleaf
    @GetMapping("/transferhistory")
    public ModelAndView getRecords(Model model) {
        ModelAndView mav = new ModelAndView("history");
        System.out.println("Whats in the model?" + model);

        authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("This is the username: "+username);
        int userid = accountService.findAccountIdByUserId(1019);
        List<Transfer> transfers = transferService.getAllToAndFromAccount(userid);
        mav.addObject("transfers",transfers );
        return mav;
    }

//    @GetMapping("/transferspending")
//    public ModelAndView findByTransferstatus(@ModelAttribute int accountfrom) {
//
//        ModelAndView pending = new ModelAndView("pending");
//        List<Transfer> pendingtransfers = transferService.findAllBystatus(accountfrom);
//        pending.addObject("pendingtransfers", pendingtransfers);
//        return pending;
//    }
    @GetMapping("/transfers/transfer")
    public Transfer findTransferById(@RequestParam int id) {
        return transferService.findById(id);
    }

    //(@ModelAttribute Transfer transfer)
    @PostMapping("/transfers/sendmoney")
    public Transfer create(@RequestBody Transfer transfer) {
        return transferService.transferBalance(transfer);
    }

    //@ModelAttribute Transfer transfer)
    @PostMapping("/transfers/request")
    public Transfer addNewTransferRequest(@RequestBody Transfer transfer) {
        return transferService.requestFundsFromUser(transfer);
    }

    @PutMapping("/transfers/update")
    public void updateTransfer(@RequestParam int statusid, @RequestParam int transferid) {
        transferService.update(statusid, transferid);
    }
}
