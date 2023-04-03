package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exceptions.InvalidRequestException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserDao;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private static final String activity = "activity";
    private static final String error = "error";
    private static final String users = "users";
    private static final String request_from = "request-from";
    private static final String pending = "pending";

    public TransferController(TransferService transferService, UserDao userDao, AccountService accountService) {
        this.transferService = transferService;
        this.userDao = userDao;
        this.accountService = accountService;

    }

    /**
     * This method retrieves a list of pending and completed transfers associated with
     * the currently logged-in user's account and passes it to a view named "activity".
     * <p>
     * Checks authentication by checking if principal is not null.
     * Uses the UserDao and AccountService to find the accountID associated
     * with the principal (current logged-in user). Calls the mergeTransferLists
     * of the TransferService to retrieve a list of pending and completed transfers.
     * Adds the list of pending and completed transfers to the model.
     *
     * @param model     object used to populate the view.
     * @param principal object represents the currently logged-in user.
     * @return A view to be rendered if the user is authenticated, otherwise a redirect to the error page.
     * @throws UserNotFoundException
     */
    @GetMapping("/activity")
    public String findTestSend(Model model, Principal principal) throws UserNotFoundException {

        if (principal.getName() != null) {

            //Find the account ID for the user
            int accountId = accountService.findAccountIdByUserId(Math.toIntExact(userDao.findIdByUsername(principal.getName())));

            //Add the pending and completed transfers for the user's account to the model.
            model.addAttribute("transfers", transferService.mergeTransferLists(accountId));

            //returns the name of the view to be rendered if authorized or error.
            return activity;
        } else {
            return error;
        }
    }

    /**
     * HTTP request handler method that maps a GET request to the '/pending' URL.
     * The Principal's name is used to get the user's id.
     * The user's id is used to identify the account belonging to that user
     * and then all transfers for that account with the status of pending
     * is returned and displayed on the view.
     *
     * @param model     Object used to pass data to the view. (list of pending transfers)
     * @param principal object represents the currently authenticated user.
     * @return the string "pending", which is the name of the view template
     * that will be used to render the response. Or the error page if the user is not authenticated.
     */
    @GetMapping("/pending")
    public String getPending(Model model, @NotNull Principal principal) {
        try {
            int userId = userDao.findIdByUsername(principal.getName());
            int accountId = accountService.findAccountIdByUserId(userId);
            model.addAttribute(pending, transferService.findAllBystatus(accountId));
            return pending;
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return error;
        }

    }

    /**
     * This method retrieves a list of users that the current user can
     * transfer funds to and passes it to a view named "send-to".
     *
     * @param model Object used to pass data to the view. (list of users)
     * @return The name of the view template that will be used to render the response.
     */
    @GetMapping("/send-to")
    public String sendTo(Model model, @NotNull Principal principal) {
        model.addAttribute(users, userDao.findTransferList());
        return "send-to";
    }

    /**
     * Endpoint for sending money from one user to another. Then redirects the user
     * to the same view.
     *
     * @param username         The username of the user to send money
     * @param amountToTransfer BigDecimal amount to transfer
     * @param principal        Current authenticated user
     * @return Redirects the view to the send-to list after transferring the funds.
     */
    @PostMapping("/send")
    public String create(@RequestParam("user") String username, @RequestParam("amount") BigDecimal amountToTransfer, Principal principal) throws InvalidRequestException {
        int sendTo = userDao.findIdByUsername(username);
        int sendFrom = accountService.findAccountIdByUserId(userDao.findIdByUsername(principal.getName()));
        transferService.sendMoney(amountToTransfer, sendTo, sendFrom);
        return "redirect:/send-to";
    }

    /**
     * @param model Object used to pass data to the view. (list of users)
     * @return The name of the view template that will be used to render the response.
     */
    @GetMapping("/request-from")
    public String requestFrom(Model model, @NotNull Principal principal) {
        model.addAttribute(users, userDao.findTransferList());
        return request_from;
    }

    /**
     * Endpoint for requesting money from a selected user. Username
     * and amount are provided by the form input.
     *
     * @param requestedUser   Name of user to request money
     * @param amountRequested BigDecimal amount to transfer
     * @param model           An object mapping a list of users, success message,
     *                        and any potential errors.
     * @param principal       Current authenticated user
     * @return Creates a transfer and sets to, from, and amount then redirects
     * the user to the request-from view.
     */
    @PostMapping("/request")
    public String transferRequest(@RequestParam("user") String requestedUser,
                                  @RequestParam("amount") String amountRequested,
                                  Model model, Principal principal) {
        try {
            String message = transferService.requestFundsFromUser(principal.getName(), requestedUser, amountRequested);
            model.addAttribute(users, userDao.findTransferList());
            model.addAttribute("message", message);
            return request_from;
        } catch (InvalidRequestException e) {
            model.addAttribute(users, userDao.findTransferList());
            model.addAttribute(error, e.getMessage());
            return request_from;
        }
    }

    /**
     * Endpoint to approve a transfer.
     *
     * @param transferstatusid The transfer status identifier.
     * @param transferid       The transfer identifier.
     * @return Updates the transfer status identifier with approved status
     * and then redirects user to the pending view.
     */
    @PostMapping("/approve")
    public String approveTransfer(@RequestParam("transferstatusid") int transferstatusid, @RequestParam("transferid") int transferid) throws InvalidRequestException {
        transferService.update(transferstatusid, transferid);
        return "redirect:/pending";
    }

    /**
     * Endpoint to reject a pending request from another user.
     *
     * @param transferstatusid The transfer status identifier
     * @param transferid       The transfer identifier
     * @return updates the transfer status to rejected and does not transfer any
     * money. Then returns the user to the pending view.
     */
    @PostMapping("/reject")
    public String rejectTransfer(@RequestParam("transferstatusid") int transferstatusid, @RequestParam("transferid") int transferid, @NotNull Principal principal) throws InvalidRequestException {
        transferService.update(transferstatusid, transferid);
        return "redirect:/pending";
    }
}
