package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "transfer")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private int id;

    @Column(name = "transfer_type_id")
    private int transfertypeid;
    @Column(name = "transfer_status_id")
    private int transferstatusid;
    @Column(name = "account_from")
    private int accountfrom;
    @Column(name = "account_to")
    private int accountto;
    @Positive(message = "Amount cannot be negative")
    @Column(name = "amount")
    private BigDecimal amount;

    @OneToOne()
    @JoinColumn(name = "transfer_type_id", insertable = false, updatable = false)
    private TransferType transferType;

    @OneToOne()
    @JoinColumn(name = "transfer_status_id", insertable = false, updatable = false)
    private TransferStatus transferStatus;

    @Autowired
    public Transfer(int id, TransferType transferType, TransferStatus transferStatus, int accountto, int accountfrom, BigDecimal amount) {
        this.id = id;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountfrom = accountfrom;
        this.accountto = accountto;
        this.amount = amount;
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

//    @Override
//    public String toString() {
//        return "Transfer: " + getAccountto() + " (" + accountto + " accountfrom" +
//                getAccountfrom() + " amount " + amount;
//    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", transfertypeid=" + transfertypeid +
                ", transferstatusid=" + transferstatusid +
                ", accountfrom=" + accountfrom +
                ", accountto=" + accountto +
                ", amount=" + amount +
                ", transferType=" + transferType +
                ", transferStatus=" + transferStatus +
                '}';
    }
}
