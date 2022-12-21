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

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    //Will need to find the syntax to get a parameter {id} in Thymeleaf
//    @GetMapping("/transferhistory")
//    public ModelAndView getRecords(HttpSession session,@RequestParam("id") int userId) {
////        public ModelAndView getRecords(@RequestParam("id") int userId, Model model) {
////        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        System.out.println("Should be the user: "+user);
//        ModelAndView mav = new ModelAndView("history", HttpStatus.valueOf(200));
//
//       int accountId = accountService.findAccountIdByUserId(userId);
//        List<Transfer> transfers = transferService.getAllToAndFromAccount(accountId);
//        mav.addObject("transfers",transfers );
//        Object user = session.getAttribute("user");
//        System.out.println("Object " + user);
//        return mav;
//    }
    @GetMapping("/transfers")
    public String getRecords(HttpSession session, Model model) {
        user = (User) session.getAttribute("user");
//        public ModelAndView getRecords(@RequestParam("id") int userId, Model model) {
        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
        model.addAttribute("transfers", transferService.getAllToAndFromAccount(accountId));
//        Object user = session.getAttribute("user");
        String username = SecurityUtils.getCurrentUsername().map(Object::toString).orElse("");
        System.out.println("SecurityUtils.getCurrentUser: " + username);

        return "transfers";
    }

//    @GetMapping("/pending")
//    public ModelAndView findByTransferstatus(@RequestParam int id, Model model) {
//        ModelAndView mav = new ModelAndView("pending");
//        int accountId = accountService.findAccountIdByUserId(id);
//        List<Transfer> pendingtransfers = transferService.findAllBystatus(accountId);
//        mav.addObject("pendingtransfers", pendingtransfers);
//        model.addAttribute("user", user);
//        return mav;
//    }

    @GetMapping("/pending")
    public String findByTransferstatus(Model model,HttpSession session) {
//        ModelAndView mav = new ModelAndView("pending");
        user = (User) session.getAttribute("user");
        int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));
        model.addAttribute("pending", transferService.findAllBystatus(accountId));
        return "pending";
    }
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
