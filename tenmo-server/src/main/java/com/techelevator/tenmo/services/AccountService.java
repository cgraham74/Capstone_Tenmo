package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService{

    @Autowired
    AccountRepository accountRepository;

    public AccountService(){
    }
    public Account findAccountByuserid(int id){
        return accountRepository.findByuserid(id);
    }

    public Account findAccountById(int id){
        return accountRepository.findById(id);
    }

}
