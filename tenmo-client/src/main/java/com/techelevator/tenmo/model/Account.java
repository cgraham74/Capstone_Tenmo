package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Account {
    private int accountid;
    private int userid;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int accountid, int userid, BigDecimal balance) {
        this.accountid = accountid;
        this.userid = userid;
        this.balance = balance;
    }

    public String currencyFormat(){
        return NumberFormat.getCurrencyInstance().format(balance);
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

    public BigDecimal getBalance() {return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
