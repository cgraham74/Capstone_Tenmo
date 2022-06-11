package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{

    //Finds account by account id
    Account findById(int id);

    //Finds account by user id
    Account findByuserid(int id);

//    @Modifying
//    @Query(value = "BEGIN TRANSACTION; " +
//            "UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
//            "UPDATE account SET balance = balance + ? WHERE account_id = ?; " +
//            "COMMIT;", nativeQuery = true)
//    void updateBalance(BigDecimal amountfrom, int accountFrom, BigDecimal amountto, int accountTo);

}
