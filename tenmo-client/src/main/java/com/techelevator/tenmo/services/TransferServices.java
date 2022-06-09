package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


public class TransferServices {

    private AccountService accountService;
    enum status {
        Pending,
        Approved,
        Rejected
    }
    enum type {
        Request,
        Send
    }
    private RestTemplate restTemplate = new RestTemplate();
    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(headers);
    }

    public Transfer sendMoney(int id, BigDecimal amountToSend){
        return new Transfer();
    }

    public Transfer requestMoney(int id, BigDecimal amountToReq){
        return new Transfer();
    }

    public Account transferMoney(Long id){
        //Handle Transfer of Funds between 2 accounts inside a Transaction
        //Update the balance of both accounts
        //Return senders new Balance


       return accountService.getBalance(id);
    }
}
