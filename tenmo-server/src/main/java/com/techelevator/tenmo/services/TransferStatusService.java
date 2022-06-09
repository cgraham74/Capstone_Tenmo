package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.repositories.TransferStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferStatusService{

    private final TransferStatusRepository repository;

    @Autowired
    public TransferStatusService(TransferStatusRepository repository) {
        this.repository = repository;
    }

    public List<TransferStatus> findAll() {
        return repository.findAll();
    }
}
