package com.techelevator.tenmo.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountid;

    @Column(name = "user_id", insertable = false, updatable = false)
    private int userid;

    @Column(name = "balance")
    private BigDecimal balance;


//    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
//    private List<Transfer>transferList;


    public Account(){

    }

    public Account(int accountid, int userid, BigDecimal balance) {
        this.accountid = accountid;
        this.userid = userid;
        this.balance = balance;
    }

    public int getAccountid() {
        return accountid;
    }

    public void setAccountid(int accountid) {
        this.accountid = accountid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


}
