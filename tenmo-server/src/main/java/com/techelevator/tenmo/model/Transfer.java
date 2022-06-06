package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int id;
    private int transfertypeid;
    private int transferstatusid;
    private int accountfrom;
    private int accountto;
    private BigDecimal amount;

    public Transfer(int id, int transfertypeid, int transferstatusid, int accountfrom, int accountto, BigDecimal amount) {
        this.id = id;
        this.transfertypeid = transfertypeid;
        this.transferstatusid = transferstatusid;
        this.accountfrom = accountfrom;
        this.accountto = accountto;
        this.amount = amount;
    }

    public Transfer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransfertypeid() {
        return transfertypeid;
    }

    public void setTransfertypeid(int transfertypeid) {
        this.transfertypeid = transfertypeid;
    }

    public int getTransferstatusid() {
        return transferstatusid;
    }

    public void setTransferstatusid(int transferstatusid) {
        this.transferstatusid = transferstatusid;
    }

    public int getAccountfrom() {
        return accountfrom;
    }

    public void setAccountfrom(int accountfrom) {
        this.accountfrom = accountfrom;
    }

    public int getAccountto() {
        return accountto;
    }

    public void setAccountto(int accountto) {
        this.accountto = accountto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
