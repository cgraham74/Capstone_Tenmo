package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.SecurityUtils;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@CrossOrigin
@Controller
@Data
public class TransferController {

    private final TransferService transferService;
    private UserDao userDao;
    private AccountService accountService;
    private User user;
    private Account account;

    public TransferController(TransferService transferService, UserDao userDao, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountService = accountService;

    }

    @GetMapping("/activity")
    public String findByActivity(Model model,HttpSession session) {
        user = (User) session.getAttribute("user");
        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
        model.addAttribute("pending", transferService.findAllBystatus(accountId));
        model.addAttribute("transfers", transferService.getAllToAndFromAccount(accountId));
        return "activity";
    }

    @GetMapping("/transfers/transfer")
    public Transfer findTransferById(@RequestParam int id) {
        return transferService.findById(id);
    }

    //(@ModelAttribute Transfer transfer)
    @PostMapping("/send")
    public Transfer create(@RequestBody Transfer transfer) {
        return transferService.transferBalance(transfer);
    }

    //@ModelAttribute Transfer transfer)
    @PostMapping("/request")
    public Transfer addNewTransferRequest(@RequestBody Transfer transfer) {
        return transferService.requestFundsFromUser(transfer);
    }

    @PutMapping("/transfers/update")
    public void updateTransfer(@RequestParam int statusid, @RequestParam int transferid) {
        transferService.update(statusid, transferid);
    }
}
