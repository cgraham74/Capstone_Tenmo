package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    //Finds account by account id
    Account findById(int id);

    //Finds account by user id
    Account findByuserid(int id);
}
