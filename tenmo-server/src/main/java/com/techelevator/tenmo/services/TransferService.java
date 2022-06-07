package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.repositories.AccountRepository;
import com.techelevator.tenmo.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService{


    @Autowired
    TransferRepository transferRepository;
    @Autowired
    AccountRepository accountRepository;

   public TransferService(){
   }

   public Transfer saveorupdatetransfer(int id, int transfertypeid, int transferstatusid, int accountfrom, int accountto, BigDecimal amount){
       BigDecimal fromBalance = accountRepository.findByuserid(accountfrom).getBalance();

       Transfer transfer = new Transfer();
       transfer.setTransfertypeid(transfertypeid);
       transfer.setTransferstatusid(transferstatusid);
       if(amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(fromBalance) == 1){
           transfer.setAccountfrom(accountfrom);
       }
       transfer.setAccountto(accountto);
       transfer.setAmount(amount);

       return transfer;
   }

    public List<Transfer> findAll() {
       return transferRepository.findAll();
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
