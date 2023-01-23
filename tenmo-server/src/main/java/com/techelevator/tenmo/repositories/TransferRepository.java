package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    List<Transfer> findAllByAccountfrom(int id);

    List<Transfer> findAllByAccountto(int id);


    @Query(value = "SELECT transfer.transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, transfer.account_from, transfer.account_to, transfer.amount\n" +
            "FROM transfer\n" +
            "JOIN account ON account.user_id = transfer.account_from\n" +
            "JOIN tenmo_user ON tenmo_user.user_id = account.user_id\n" +
            "WHERE tenmo_user.user_id = ?1 AND transfer.transfer_type_id = 2;", nativeQuery = true)
    List<Transfer> getAllSendMoneyTo(int userId);

    @Query(value = "SELECT transfer.transfer_id, transfer.transfer_status_id, transfer.transfer_type_id, transfer.account_from, \n" +
            "transfer.account_to, transfer.amount, transfer_status.transfer_status_desc, transfer_type.transfer_type_desc \n" +
            "FROM transfer \n" +
            "LEFT JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id\n" +
            "LEFT JOIN transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id\n" +
            "WHERE transfer.transfer_id = ?1" ,nativeQuery = true )
    Transfer findByIdDesc(int id);

    Transfer findById(int id);

    @Query(value = "SELECT * FROM transfer WHERE transfer_status_id = 1 AND account_from = ?1", nativeQuery = true)
    List<Transfer> findByStatus(int accountfrom);

    @Modifying
    @Transactional
    @Query(value = "UPDATE transfer SET transfer_status_id = ?1 WHERE transfer_id = ?2 ", nativeQuery = true)
    void update(int statusid, int transferid);

}
