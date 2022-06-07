package com.techelevator.tenmo.repositories;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TansferTypeRepository extends JpaRepository<TransferType, Integer> {
}
