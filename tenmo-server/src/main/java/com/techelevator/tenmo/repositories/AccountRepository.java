package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    Account findById(int id);

    Account findByuserid(int id);

}
