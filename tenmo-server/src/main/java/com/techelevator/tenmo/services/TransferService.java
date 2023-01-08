package com.techelevator.tenmo.services;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransferService{

    private final int REQUEST = 1;
    private final int SEND = 2;
    private final int RECEIVE = 1;
    private final int APPROVED = 2;
    private final int PENDING = 1;
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserDao userDao;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository, UserDao userDao,
                           AccountService accountService) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userDao = userDao;
        this.accountService = accountService;
    }

    public Transfer save(Transfer transfer){
       return transferRepository.save(transfer);
   }

    /**
     *
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
     * @param transfer
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public Transfer transferBalance(Transfer transfer) {

        //Getting amount to transfer between accounts
        BigDecimal moneyToSend = transfer.getAmount();

        // Getting the id of both the Current user (sender) and Target user (recipient)
        Account accountOfCurrentUser = accountService.findAccountById(transfer.getAccountfrom());
        Account accountOfTargetUser = accountService.findAccountById(transfer.getAccountto());

        //Updates the balances of from and to users with transfer transaction
        if (moneyToSend.compareTo(accountOfCurrentUser.getBalance()) <= 0 && moneyToSend.compareTo(BigDecimal.ZERO) > 0){

            //Verifying user and recipient differs - else throwing an exception with personalized message
            if(accountOfCurrentUser.getUserid() != accountOfTargetUser.getUserid()) {

                //Adding the amount to one account and subtracting from another
                accountOfCurrentUser.setBalance(accountOfCurrentUser.getBalance().subtract(moneyToSend));
                accountOfTargetUser.setBalance(accountOfTargetUser.getBalance().add(moneyToSend));

                //Saving new balances to the repository
                accountRepository.save(accountOfCurrentUser);
                accountRepository.save(accountOfTargetUser);

                return transfer;
            }

            throw new RuntimeException("Cannot send money to self");
        }
        throw new RuntimeException("Insufficient Funds for Transfer");
    }

    /**
     * Calls the transferBalance method to transfer
     * money from one account to another within a database transaction
     * Saves the details of the transfer to the repository
     * @param moneyToSend BigDecimal amount to transfer
     * @param toUser Integer representing the user to whom the money will be sent
     * @param fromUser Integer representing the user who is sending the money
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public void sendMoney(BigDecimal moneyToSend, int toUser, int fromUser ) {
        Transfer transfer = new Transfer();
            transfer.setAccountfrom(fromUser);
            transfer.setAccountto(toUser);
            transfer.setAmount(moneyToSend);
            transfer.setTransfertypeid(SEND);
            transfer.setTransferstatusid(APPROVED);
            transferRepository.save(transferBalance(transfer));
    }

    /**
     *
     * @param transfer
     * @return
     */
    public Transfer requestFundsFromUser(Transfer transfer){

        if (transfer.getAccountto() != transfer.getAccountfrom()){
            if(transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                transfer.setTransferstatusid(PENDING);
                transfer.setTransfertypeid(REQUEST);
                return transferRepository.save(transfer);
            }
            throw new RuntimeException("Request must be larger than 0");
        }
        throw new RuntimeException("Invalid Request - Cannot request from self");
    }

    /**
     *
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
     *
     * @param statusid
     * @param transferid
     */
    @Transactional
    public void update(int statusid, int transferid) {
        Transfer transfer = transferRepository.findById(transferid);
        if(statusid == APPROVED){
            transferBalance(transfer);
        }
        transferRepository.update(statusid,transferid);
    }

    /**
     *
     * @param id
     * @return
     */
    public List<Transfer> getAllToAndFromAccount(int id){
        List<Transfer>from = transferRepository.findAllByAccountfrom(id);
        List<Transfer>to = transferRepository.findAllByAccountto(id);

        List<Transfer> list = new ArrayList<>();
        Stream.of(from, to).forEach(list::addAll);
        return list;
    }
}
