package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.repositories.TransferTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//Service layer - Abstracts away business logic from the controller
@Service
public class TransferTypeService {

    private final TransferTypeRepository repository;

    @Autowired
    public TransferTypeService(TransferTypeRepository repository) {
        this.repository = repository;
    }

    public List<TransferType> findAll() {
        return repository.findAll();
    }

    public TransferType findById(int id) {
        return repository.findById(id);
    }
}
