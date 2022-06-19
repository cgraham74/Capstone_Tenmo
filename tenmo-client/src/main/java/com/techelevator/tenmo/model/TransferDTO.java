package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDTO {
    private int id;
    private TransferType transferType;
    private TransferStatus transferStatus;
    private int accountfrom;
    private int accountto;
    private BigDecimal amount;


    public TransferDTO(int id, TransferType transferType, TransferStatus transferStatus, int accountfrom, int accountto, BigDecimal amount) {
        this.id = id;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountfrom = accountfrom;
        this.accountto = accountto;
        this.amount = amount;
    }

    public TransferDTO() {
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "id=" + id +
                ", transferType=" + transferType +
                ", transferStatus=" + transferStatus +
                ", accountfrom=" + accountfrom +
                ", accountto=" + accountto +
                ", amount=" + amount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
