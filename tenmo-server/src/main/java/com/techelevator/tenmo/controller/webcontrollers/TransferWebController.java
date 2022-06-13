package com.techelevator.tenmo.controller.webcontrollers;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferTypeRepository;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("webtransfer/")
public class TransferWebController {

    private final TransferService transferService;
    private UserDao userDao;
    private AccountRepository accountRepository;
    private TransferTypeRepository transferTypeRepository;
    private AccountService accountService;

    @Autowired
    public TransferWebController(TransferService transferService, UserDao userDao, AccountRepository accountRepository, TransferTypeRepository transferTypeRepository, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountRepository = accountRepository;
        this.transferTypeRepository = transferTypeRepository;
        this.accountService = accountService;
    }

    @GetMapping("/test")
    public String getAllTransfers(Model model){
        List<Transfer> transfers = this.transferService.findAll();
        model.addAttribute("transfers", transfers);
        return "transfer";
    }
}
