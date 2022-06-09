package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    List<Transfer> findAllByAccountfrom(int id);

    List<Transfer> findAllByAccountto(int id);

    Transfer findById(int id);
}
