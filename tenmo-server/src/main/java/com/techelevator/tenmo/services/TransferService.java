package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService{

    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    private UserDao userDao;

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

    public List<Transfer> findAll() {
       return transferRepository.findAll();
    }

    public List<Transfer> findAllByAccountfrom(int id) {
       return transferRepository.findAllByAccountfrom(id);
    }

    public List<Transfer> findAllByAccountto(int id) {
       return transferRepository.findAllByAccountto(id);
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
        Account accountofTargetuser = accountService.findAccountById(transfer.getAccountto());

        //Updates the balances of from and to users with transfer transaction
        if (moneyToSend.compareTo(accountOfCurrentUser.getBalance()) <= 0 && moneyToSend.compareTo(BigDecimal.ZERO) > 0){

            //Verifying user and recipient differs - else throwing an exception with personalized message
            if(accountOfCurrentUser.getUserid() != accountofTargetuser.getUserid()) {

                //Adding the amount to one account and subtracting from another
                accountOfCurrentUser.setBalance(accountOfCurrentUser.getBalance().subtract(moneyToSend));
                accountofTargetuser.setBalance(accountofTargetuser.getBalance().add(moneyToSend));

                //Saving new balances to the repository
                accountRepository.save(accountOfCurrentUser);
                accountRepository.save(accountofTargetuser);

                //Saving a transaction of the transfer
                transferRepository.save(transfer);
                return transfer;
            }

            throw new RuntimeException("Cannot send money to self");
        }
        throw new RuntimeException("Insufficient Funds for Transfer");
    }

    public Transfer requestFundsFromUser(Transfer transfer){
        transfer.setTransferstatusid(1);
        transfer.setTransfertypeid(1);
        return transferRepository.save(transfer);
    }

    public List<Transfer> findAllBystatus(int accountfrom) {

        return transferRepository.findByStatus(accountfrom);
    }
}
