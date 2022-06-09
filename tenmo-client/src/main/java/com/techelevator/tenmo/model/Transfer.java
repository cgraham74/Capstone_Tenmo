package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int transferstatusid;
    private int transfertypeid;
    private int accountto;
    private int accountfrom;
    private BigDecimal amount;

    public Transfer(int id, int transferstatusid, int transfertypeid, int accountto, int accountfrom, BigDecimal amount) {
        this.id = id;
        this.transferstatusid = transferstatusid;
        this.transfertypeid = transfertypeid;
        this.accountto = accountto;
        this.accountfrom = accountfrom;
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

    public int getTransferstatusid() {
        return transferstatusid;
    }

    public void setTransferstatusid(int transferstatusid) {
        this.transferstatusid = transferstatusid;
    }

    public int getTransfertypeid() {
        return transfertypeid;
    }

    public void setTransfertypeid(int transfertypeid) {
        this.transfertypeid = transfertypeid;
    }

    public int getAccountto() {
        return accountto;
    }

    public void setAccountto(int accountto) {
        this.accountto = accountto;
    }

    public int getAccountfrom() {
        return accountfrom;
    }

    public void setAccountfrom(int accountfrom) {
        this.accountfrom = accountfrom;
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
                ", status='" + transferstatusid + '\'' +
                ", type='" + transfertypeid + '\'' +
                ", accountTo=" + accountto +
                ", accountFrom=" + accountfrom +
                ", amount=" + amount +
                '}';
    }
}
