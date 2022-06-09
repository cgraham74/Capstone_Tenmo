package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int status;
    private int type;
    private int accountTo;
    private int accountFrom;
    private BigDecimal amount;

    public Transfer(int id,int status, int type, int accountTo, int accountFrom, BigDecimal amount) {
        this.id = id;
        this.status = status;
        this.type = type;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
    }

    public Transfer(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", accountTo=" + accountTo +
                ", accountFrom=" + accountFrom +
                ", amount=" + amount +
                '}';
    }
}
