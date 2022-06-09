package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TransferQueue;

public class AccountService {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(headers);
    }


    public Account getBalance(Long id){
        var account = new Account();
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "/account/balancebyuserid?id=" + id, HttpMethod.GET, makeEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
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
    public List<Transfer> transferList(Long id) {
        List<Transfer>list = new ArrayList<>();
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/transferfrom?id=" + id, HttpMethod.GET, makeEntity(), Transfer[].class );
            list = Arrays.asList((Transfer[])Objects.requireNonNull((Transfer[])response.getBody()));
            System.out.println("Testing list from accountService.transferlist: " + list.size());
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

    public Account getAccountFromUserId(Long id){
        Account account = new Account();
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "account/balancebyuserid?id=" + id, HttpMethod.GET, makeEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }
}
