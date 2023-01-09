package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exceptions.InvalidRequestException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class TransferService {

    private final int REQUEST;
    private final int REJECTED;
    private final int SEND;
    private final int APPROVED;
    private final int PENDING;
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserDao userDao;
    Locale locale;
    NumberFormat currencyFormat;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository, UserDao userDao,
                           AccountService accountService) {
        this.PENDING = 1;
        this.APPROVED = 2;
        this.REJECTED = 3;
        this.SEND = 2;
        this.REQUEST = 1;
        this.userDao = userDao;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.locale = Locale.getDefault();
        this.currencyFormat = NumberFormat.getCurrencyInstance(locale);

    }

    public Transfer save(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    /**
     * @param id
     * @return
     */
    public Transfer findById(int id) {
        return transferRepository.findById(id);
    }

    /**
     * Handles Transfers from one account to another
     * If the transfer fails on one account, the transfer
     * will not happen for either account
     *
     * @param transfer
     * @return
     */
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public Transfer transferBalance(Transfer transfer) throws InvalidRequestException {

        //Getting amount to transfer between accounts
        BigDecimal moneyToSend = transfer.getAmount();

        // Getting the id of both the Current user (sender) and Target user (recipient)
        Account accountOfCurrentUser = accountService.findAccountById(transfer.getAccountfrom());
        Account accountOfTargetUser = accountService.findAccountById(transfer.getAccountto());

        //Updates the balances of from and to users with transfer transaction
        if (moneyToSend.compareTo(accountOfCurrentUser.getBalance()) <= 0 && moneyToSend.compareTo(BigDecimal.ZERO) > 0) {

            //Verifying user and recipient differs - else throwing an exception with personalized message
            if (accountOfCurrentUser.getUserid() != accountOfTargetUser.getUserid()) {

                //Adding the amount to one account and subtracting from another
                accountOfCurrentUser.setBalance(accountOfCurrentUser.getBalance().subtract(moneyToSend));
                accountOfTargetUser.setBalance(accountOfTargetUser.getBalance().add(moneyToSend));

                //Saving new balances to the repository
                accountRepository.save(accountOfCurrentUser);
                accountRepository.save(accountOfTargetUser);

                return transfer;
            }

            throw new InvalidRequestException("Cannot send money to self");
        }
        throw new InvalidRequestException("Insufficient Funds for Transfer");
    }

    /**
     * Calls the transferBalance method to transfer
     * money from one account to another within a database transaction
     * Saves the details of the transfer to the repository
     *
     * @param moneyToSend BigDecimal amount to transfer
     * @param toUser      Integer representing the user to whom the money will be sent
     * @param fromUser    Integer representing the user who is sending the money
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void sendMoney(BigDecimal moneyToSend, int toUser, int fromUser) throws InvalidRequestException {
        Transfer transfer = new Transfer();
        transfer.setAccountfrom(fromUser);
        transfer.setAccountto(toUser);
        transfer.setAmount(moneyToSend);
        transfer.setTransfertypeid(SEND);
        transfer.setTransferstatusid(APPROVED);
        transferRepository.save(transferBalance(transfer));
    }

    /**
     * Request funds from a user.
     * @param requestor The username of the requestor
     * @param requestee The username of the requestee
     * @param amountRequested the amount requested, as a string
     * @return request message to let the user know it was successful
     * @throws InvalidRequestException if the request is invalid(e.g. requesting from self
     * invalid amount, not a digit)
     */
    public String requestFundsFromUser(String requestor, String requestee, String amountRequested)
            throws InvalidRequestException {
        Transfer transfer = new Transfer();

        // Get account IDs for the requestor and requestee
        int requestorAccountId = accountService.findAccountIdByUserId(userDao.findIdByUsername(requestor));
        int requesteeAccountId = accountService.findAccountIdByUserId(userDao.findIdByUsername(requestee));

        // Set the account IDs for the transfer
        transfer.setAccountto(requestorAccountId);
        transfer.setAccountfrom(requesteeAccountId);

        try {
            BigDecimal amount = new BigDecimal(amountRequested);
            transfer.setAmount(amount);
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("Invalid Request - Please enter a valid amount in digits.");
        }

        if (requestorAccountId == requesteeAccountId) {
            throw new InvalidRequestException("Invalid Request - Cannot request from self.");
        } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Invalid Request - Please enter valid amount greater than 0.");
        }

        transfer.setTransferstatusid(PENDING);
        transfer.setTransfertypeid(REQUEST);
        transferRepository.save(transfer);
        return "Requested amount: " + currencyFormat.format(transfer.getAmount()) + " from " + requestee;
    }

    /**
     * @param accountfrom
     * @return
     */
    public List<Transfer> findAllBystatus(int accountfrom) {
        return transferRepository.findByStatus(accountfrom);
    }
//
//    public Transfer saveUpdate(Transfer transfer) {
//        return transferRepository.save(transfer);
//    }

    /**
     * Updates a Transfer with the status id that is passed in.
     * If that status id is approved. Then the transferBalance method
     * is called to handle the transfer of money from one account to the other
     * otherwise- the transfer is updated with the id (rejected) from the view
     * @param statusid The new status id to update the transfer
     * @param transferid the id of a specific transfer to be updated
     */
    @Transactional
    public void update(int statusid, int transferid) throws InvalidRequestException {
        Transfer transfer = transferRepository.findById(transferid);
        if (statusid == APPROVED) {
            transferBalance(transfer);
        }
        transferRepository.update(statusid, transferid);
    }

    /**
     * @param userId
     * @return
     */
    public List<Transfer> getAllToAndFromAccount(int userId) {
        List<Transfer> from = transferRepository.findAllByAccountfrom(userId);
        List<Transfer> to = transferRepository.findAllByAccountto(userId);

        List<Transfer> list = new ArrayList<>();
        Stream.of(from, to).forEach(list::addAll);
        return list;
    }
}
