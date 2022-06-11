package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    List<Transfer> findAllByAccountfrom(int id);

    List<Transfer> findAllByAccountto(int id);

    Transfer findById(int id);

    @Query(value = "select * from transfer join account on account_id in(transfer.account_from, transfer.account_to) join tenmo_user on account.user_id = tenmo_user.user_id where tenmo_user.username = ?1", nativeQuery = true)
    List<Transfer> findByUser(String username);

    @Query(value = "SELECT * FROM transfer WHERE transfer_status_id = 1 AND accountfrom = ?1", nativeQuery = true)
    List<Transfer> findByStatus(int accountfrom);
}
