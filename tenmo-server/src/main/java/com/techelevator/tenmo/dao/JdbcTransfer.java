package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;

@Component
public class JdbcTransfer implements TransferDao{

    JdbcTemplate jdbcTemplate;

    public JdbcTransfer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTransfer() {
    }


    @Override
    public Transfer transferBalanceFromUserToUser(BigDecimal balance, User sender, User receiver) {
        Transfer transfer = new Transfer();
        String sql = "BEGIN TRANSACTION;\n" +
                "        UPDATE account SET balance = balance - ? WHERE account_number = ?;\n" +
                "        UPDATE account SET balance = balance + ? WHERE account_number = ?;\n" +
                "        COMMIT;";
        jdbcTemplate.update(sql, balance, sender, balance, receiver);

        //I can't send a zero or negative amount.
        //I can't send more TE Bucks than I have in my account.
        if (balance.compareTo(BigDecimal.ZERO)> 0){


        }
        return null;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setId(rs.getInt("transfer_id"));
        transfer.setTransfertypeid(rs.getInt("transfer_type_id"));
        transfer.setTransferstatusid(rs.getInt("transfer_status_id"));
        transfer.setAccountfrom(rs.getInt("account_from"));
        transfer.setAccountto(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }

}
