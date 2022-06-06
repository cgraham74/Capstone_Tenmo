package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    public static final String API_BASE_URL = "http://localhost:8080/balance";
    private RestTemplate restTemplate = new RestTemplate();

    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<String>(headers);
    }

    public Account getBalance(Long id){
        var account = new Account();
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "?id=" + id, HttpMethod.GET, makeEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }
}
