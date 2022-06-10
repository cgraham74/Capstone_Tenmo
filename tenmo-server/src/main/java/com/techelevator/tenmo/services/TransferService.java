package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransferService{


    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private UserDao userDao;

    @Autowired
    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository, UserDao userDao) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userDao = userDao;
    }


    public Transfer save(Transfer transfer){
       return transferRepository.save(transfer);
   }

    public List<Transfer> findAll() {
       return transferRepository.findAll();
    }

    public List<Transfer> findAllByAccountfrom(int id) {
       return transferRepository.findAllByAccountfrom(id);
    }

    public List<Transfer> findAllByAccountto(int id) {
       return transferRepository.findAllByAccountto(id);
    }

    public Transfer findById(int id) {
       return transferRepository.findById(id);
    }


//    @Override
//    public Transfer transferBalanceFromUserToUser(BigDecimal balance, User sender, User receiver) {
//        Transfer transfer = new Transfer();
//        String sql = "BEGIN TRANSACTION;\n" +
//                "        UPDATE account SET balance = balance - ? WHERE account_number = ?;\n" +
//                "        UPDATE account SET balance = balance + ? WHERE account_number = ?;\n" +
//                "        COMMIT;";
//        jdbcTemplate.update(sql, balance, sender, balance, receiver);
//
//        //I can't send a zero or negative amount.
//        //I can't send more TE Bucks than I have in my account.
//        if (balance.compareTo(BigDecimal.ZERO)> 0){
//
//        }
//        return transfer;
//    }


//    private Transfer mapRowToTransfer(SqlRowSet rs){
//        Transfer transfer = new Transfer();
//        transfer.setId(rs.getInt("transfer_id"));
//        transfer.setTransfertypeid(rs.getInt("transfer_type_id"));
//        transfer.setTransferstatusid(rs.getInt("transfer_status_id"));
//        transfer.setAccountfrom(rs.getInt("account_from"));
//        transfer.setAccountto(rs.getInt("account_to"));
//        transfer.setAmount(rs.getBigDecimal("amount"));
//        return transfer;
//    }

}
