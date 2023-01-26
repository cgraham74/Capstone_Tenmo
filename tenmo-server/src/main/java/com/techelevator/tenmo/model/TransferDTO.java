package com.techelevator.tenmo.model;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

public class TransferDTO {

    private int id;
    private int transfertypeid;
    private int transferstatusid;
    private String accountnamefrom;
    private String accountNameto;
    private BigDecimal amount;
    private String transferTypeName;
    private String transferStatusName;


    public TransferDTO(int id, TransferType transferType, TransferStatus transferStatus, String accountnamefrom, String accountNameto, BigDecimal amount) {
        this.id = id;
//        this.transferstatusid = transferstatusid;
//        this.transfertypeid = transfertypeid;
        this.accountnamefrom = accountnamefrom;
        this.accountNameto = accountNameto;
        this.amount = amount;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
    }

    @OneToOne()
    @JoinColumn(name = "transfer_type_id", insertable = false, updatable = false)
    private TransferType transferType;

    @OneToOne()
    @JoinColumn(name = "transfer_status_id", insertable = false, updatable = false)
    private TransferStatus transferStatus;

    public TransferDTO(){

    }


    public TransferType getTransferType() {
        return transferType;
    }


    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }


    public TransferStatus getTransferStatus() {
        return transferStatus;
    }


    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
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


    public String getAccountnamefrom() {
        return accountnamefrom;
    }

    public void setAccountnamefrom(String accountnamefrom) {
        this.accountnamefrom = accountnamefrom;
    }

    public String getAccountNameto(){
        return accountNameto;
    }

    public void setAccountNameto(String accountNameto) {
        this.accountNameto = accountNameto;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

//    @Override
//    public String toString() {
//        return "TransferDTO{" +
//                "id=" + id +
//                ", transfertypeid=" + transfertypeid +
//                ", transferstatusid=" + transferstatusid +
//                ", accountfrom=" + accountnamefrom +
//                ", accountto=" + accountNameto +
//                ", amount=" + amount +
//                '}';
//    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "id=" + id +
                ", accountnamefrom='" + accountnamefrom + '\'' +
                ", accountNameto='" + accountNameto + '\'' +
                ", amount=" + amount +
                ", transferType=" + transferType +
                ", transferStatus=" + transferStatus +
                '}';
    }
}
