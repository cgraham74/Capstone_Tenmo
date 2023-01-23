package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {

    private int id;
    private int transfertypeid;
    private int transferstatusid;
    private String accountfrom;
    private String accountto;
    private BigDecimal amount;


    public TransferDTO(int id, int transfertypeid, int transferstatusid, String accountfrom, String accountto, BigDecimal amount) {
        this.id = id;
        this.transferstatusid = transferstatusid;
        this.transfertypeid = transfertypeid;
        this.accountfrom = accountfrom;
        this.accountto = accountto;
        this.amount = amount;
    }

    public TransferDTO(){

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

    public String getAccountfrom() {
        return accountfrom;
    }

    public void setAccountfrom(String accountfrom) {
        this.accountfrom = accountfrom;
    }

    public String getAccountto() {
        return accountto;
    }

    public void setAccountto(String accountto) {
        this.accountto = accountto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "id=" + id +
                ", transfertypeid=" + transfertypeid +
                ", transferstatusid=" + transferstatusid +
                ", accountfrom=" + accountfrom +
                ", accountto=" + accountto +
                ", amount=" + amount +
                '}';
    }
}
