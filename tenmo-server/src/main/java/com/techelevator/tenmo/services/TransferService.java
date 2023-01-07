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

    private final int SEND = 2;
    private final int RECEIVE = 1;
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

    public Transfer findById(int id) {
       return transferRepository.findById(id);
    }

    //Transactional annotation being used for the balance transfer transaction so that if any methods in here fail to add *AND* subtract
    // then they both fail and balances are not updated
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

                //Saving a new transaction of the transfer only if it is a send request
                if(transfer.getTransfertypeid() == SEND) {
                    transferRepository.save(transfer);
                }
                return transfer;
            }

            throw new RuntimeException("Cannot send money to self");
        }
        throw new RuntimeException("Insufficient Funds for Transfer");
    }

    //Request funds from a different account. Sets type to request, sets status to pending
    public Transfer requestFundsFromUser(Transfer transfer){

        if (transfer.getAccountto() != transfer.getAccountfrom()){
            if(transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                transfer.setTransferstatusid(1);
                transfer.setTransfertypeid(1);
                return transferRepository.save(transfer);
            }
            throw new RuntimeException("Request must be larger than 0");
        }
        throw new RuntimeException("Invalid Request - Cannot request from self");
    }

    public List<Transfer> findAllBystatus(int accountfrom) {
        return transferRepository.findByStatus(accountfrom);
    }

    public Transfer saveUpdate(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    public void update(int usid, int transferid) {
        transferRepository.update(usid,transferid);
    }

    public List<Transfer> getAllToAndFromAccount(int id){
        List<Transfer>from = transferRepository.findAllByAccountfrom(id);
        List<Transfer>to = transferRepository.findAllByAccountto(id);
        List<Transfer> list = new ArrayList<>();
        Stream.of(from, to).forEach(list::addAll);
        return list;
    }
}
