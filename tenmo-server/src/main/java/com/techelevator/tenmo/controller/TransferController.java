package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exceptions.UserNotFoundException;
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
import java.math.BigDecimal;
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

            model.addAttribute("transfers", transferService.getAllToAndFromAccount(accountId));

            //returns the name of the view to be rendered if authorized or error.
            return "activity";
        } else {
            return "error";
        }
    }

    @GetMapping("/pending")
    public String getPending(Model model, Principal principal) {
        int accountId = userDao.findIdByUsername(principal.getName());
        model.addAttribute("pending", transferService.findAllBystatus(accountId));
        return "pending";
    }

    @GetMapping("/transfers/transfer")
    public Transfer findTransferById(@RequestParam int id) {
        return transferService.findById(id);
    }

    @GetMapping("/send-to")
    public String sendTo(Model model) {
        model.addAttribute("users", userDao.findTransferList());
        return "send-to";
    }

    @GetMapping("/request-from")
    public String requestFrom(Model model) {
        model.addAttribute("users", userDao.findTransferList());
        return "request-from";
    }

//    //(@ModelAttribute Transfer transfer)
//    @PostMapping("/send")
//    public Transfer create(@RequestBody Transfer transfer) {
//        //Get all of the sendto user's information
//        //Get the amount to transfer
//        return transferService.transferBalance(transfer);
//    }

    //(@ModelAttribute Transfer transfer)
    @PostMapping("/send")
    public String create(@RequestParam("user")String username,@RequestParam("amount") BigDecimal amountToTransfer, Principal principal) throws UserNotFoundException {
        int userId = userDao.findIdByUsername(username);
        Transfer transfer = new Transfer();
        transfer.setAccountfrom(accountService.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
        transfer.setTransfertypeid(2);
        transfer.setTransferstatusid(2);
        transfer.setAccountto(accountService.findAccountIdByUserId(userId));
        transfer.setAmount(amountToTransfer);
        transferService.transferBalance(transfer);
        return "redirect:/send-to";
    }

    //@ModelAttribute Transfer transfer)
    @PostMapping("/request")
    public String addNewTransferRequest(@RequestParam("user")String username,@RequestParam("amount") BigDecimal amountToTransfer, Principal principal) {
        int userId = userDao.findIdByUsername(username);
        Transfer transfer = new Transfer();
        transfer.setAccountto(accountService.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
        transfer.setAccountfrom(accountService.findAccountIdByUserId(userId));
        transfer.setAmount(amountToTransfer);
        transferService.requestFundsFromUser(transfer);
        return "redirect:/request-from";
    }

    @PutMapping("/transfers/update")
    public void updateTransfer(@RequestParam int statusid, @RequestParam int transferid) {
        transferService.update(statusid, transferid);
    }
}
