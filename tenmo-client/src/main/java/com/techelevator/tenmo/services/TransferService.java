package com.techelevator.tenmo.services;

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
    private String authToken = null;
    private ConsoleService consoleService;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public HttpEntity makeEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<String>(headers);
    }
    public HttpEntity makeEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    //Request money from another user (NOT SELF)
    public Transfer requestMoney(BigDecimal amount, int accountto, int accountfrom){
        Transfer transfer = new Transfer(1,1, accountto, accountfrom,amount);
        HttpEntity<Transfer> entity = makeEntity(transfer);
        Transfer newTransfer = null;
        try{
            newTransfer = restTemplate.postForObject(API_BASE_URL + "transfer/request", entity, Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return newTransfer;
    }

    //Get a single transfer by its id

    public Transfer getTransferById(int id){
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/transferid?id=" + id, HttpMethod.GET, makeEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    //Send Money to another user
    public Transfer transferMoney(int accountfrom, int accountto, BigDecimal amount){
        Transfer transfer = new Transfer(2, 2, accountto, accountfrom, amount);
        HttpEntity<Transfer> entity = makeEntity(transfer);
        Transfer newTransfer = null;

        try{
            newTransfer= restTemplate.postForObject(API_BASE_URL + "transfer/transferfunds", entity, Transfer.class);

        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
       return newTransfer;
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
    //Get list of Transfers with a pending status from the server
    public List<Transfer> pendingTransfers(Long id){
        List<Transfer> list = new ArrayList<>();
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + "transfer/pending?accountfrom=" + id , HttpMethod.GET, makeEntity(), Transfer[].class);
            list = Arrays.asList((Transfer[])Objects.requireNonNull((Transfer[])response.getBody()));
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return list;
    }

    public Transfer singlePendingTransfer(Long id){
        Transfer transfer = new Transfer();
        try{
            ResponseEntity<Transfer> response = restTemplate.exchange(API_BASE_URL + "transfer/pendingbyid?id=" + id, HttpMethod.GET, makeEntity(), Transfer.class);
            transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return transfer;
    }


    public boolean update(Transfer transfer) {
        HttpEntity<Transfer> entity = makeEntity(transfer);
        boolean success = false;
        try{
            restTemplate.put(API_BASE_URL + "transfer/update?statusid=" + transfer.getTransferstatusid() + "&transferid=" + transfer.getId(), entity);
            success = true;
        }catch (RestClientResponseException | ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }
        return success;
    }



}
