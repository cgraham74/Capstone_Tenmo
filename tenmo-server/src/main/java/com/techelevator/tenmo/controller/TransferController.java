package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
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
import java.security.Principal;

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

    /**
     * Handles an HTTP GET request to the activity endpoint.
     *
     * @param model   a Model object used to store data for the view.
     * @param session an HttpSession object used to store the data in the
     *                user' session.
     * @return The name of the view to be rendered in response to the GET request.
     */
    @GetMapping("/activity")
    public String findActivity(Model model, HttpSession session, Principal principal) {
        //Retrieves the user object from the session
        user = (User) session.getAttribute("user");

        if (user.getUsername().equals(principal.getName())) {

            //Find the account ID for the user
            int accountId = accountService.findAccountIdByUserId(Math.toIntExact(user.getId()));

            //Add the pending and completed transfers for the user's account to the model.
            model.addAttribute("pending", transferService.findAllBystatus(accountId));
            model.addAttribute("transfers", transferService.getAllToAndFromAccount(accountId));

            //returns the name of the view to be rendered if authorized or error.
            return "activity";
        } else {
            return "error";
        }
    }

    @GetMapping("/transfers/transfer")
    public Transfer findTransferById(@RequestParam int id) {
        return transferService.findById(id);
    }

    @GetMapping("send-to")
    public String sendTo(Model model) {
        model.addAttribute("users", userDao.findTransferList());
        return "send-to";
    }

    @GetMapping("request-from")
    public String requestFrom(Model model) {
        model.addAttribute("users", userDao.findTransferList());
        return "request-from";
    }
    //(@ModelAttribute Transfer transfer)
    @PostMapping("/send")
    public Transfer create(@RequestBody Transfer transfer) {
        //Get all of the sendto user's information
        //Get the amount to transfer
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
