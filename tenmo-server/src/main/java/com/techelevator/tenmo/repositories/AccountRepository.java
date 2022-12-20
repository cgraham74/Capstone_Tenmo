package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    //Finds account by account id
    Account findById(int id);

    //Finds account by user id
    Account findByuserid(int id);

    @Query(value = "SELECT account_id FROM account \n" +
            "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
            "WHERE account.user_id = ? ", nativeQuery = true)
    int findAccountIdByUserId(int id);


    @Query(value = "SELECT balance FROM account \n" +
            "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
            "WHERE account.user_id = ? ", nativeQuery = true)
    int findAccountBalanceByUserId(int id);
}
