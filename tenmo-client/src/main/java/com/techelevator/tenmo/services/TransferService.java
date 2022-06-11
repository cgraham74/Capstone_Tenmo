package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AccountService accountService;

    private Transfer transfer;



    enum status {
        Pending,
        Approved,
        Rejected
    }
    enum type {
        Request,
        Send
    }

    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(headers);
    }

    //Transfers money from one account to another in tandem




    public Transfer requestMoney(int id, BigDecimal amountToReq){
        return new Transfer();
    }

    public Account transferMoney(Long id){
        //Handle Transfer of Funds between 2 accounts inside a Transaction
        //Update the balance of both accounts
        //Return senders new Balance


       return accountService.getBalance(id);
    }

    public List<User> displayRegisteredUsers(String username){
        List<User> userList = new ArrayList<>();
        try{
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "transferlist?username=" + username, HttpMethod.GET, makeEntity(), User[].class);
            userList = Arrays.asList(Objects.requireNonNull(response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return userList;
    }

    //Get list of Transfers from currentUser
    public List<Transfer> transferFromList(Long id) {
        List<Transfer>list = new ArrayList<>();
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/transferfrom?id=" + id, HttpMethod.GET, makeEntity(), Transfer[].class );
            list = Arrays.asList((Transfer[])Objects.requireNonNull((Transfer[])response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

    //Get a list of transfers to currentUser
    public List<Transfer> transferToList(Long id) {
        List<Transfer>list = new ArrayList<>();
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/transferto?id=" + id, HttpMethod.GET, makeEntity(), Transfer[].class );
            list = Arrays.asList((Transfer[])Objects.requireNonNull((Transfer[])response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

}
