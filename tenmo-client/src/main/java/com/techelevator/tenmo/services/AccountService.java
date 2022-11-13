package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class AccountService {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<String>(headers);
    }

    public int getAccountIdByUserId(int id) {
        int accountId = 0;
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(API_BASE_URL + "accounts/account?id=" + id, HttpMethod.GET, makeEntity(), Integer.class);
            accountId = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accountId;
    }

    public Account getAccount(Long id){
        var account = new Account();
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "accounts/balance?id=" + id, HttpMethod.GET, makeEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public List<User> getListOfUsers(){
        List<User> userList = new ArrayList<>();
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "users/list", HttpMethod.GET, makeEntity(), User[].class );
            userList = Arrays.asList((User[])Objects.requireNonNull((User[])response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return userList;
    }

    public User getUserByAccountId(int id){
        User user = new User();
        try {
            ResponseEntity<User> response = restTemplate.exchange(API_BASE_URL + "users/account?id=" + id, HttpMethod.GET, makeEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return user;
    }
}
