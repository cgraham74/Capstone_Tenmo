package com.techelevator.tenmo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transfer_status")
public class TransferStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_status_id")
    private int transferstatusid;

    @Column(name = "transfer_status_desc")
    private String transferstatusdesc;


    @OneToMany()
    @JoinColumn(name = "transfer_status_id", insertable = false, updatable = false)
    private List<Transfer> transfer;

    public TransferStatus() {
    }

    public TransferStatus(int transferstatusid, String transferstatusdesc) {
        this.transferstatusid = transferstatusid;
        this.transferstatusdesc = transferstatusdesc;
    }

    public int getTransferstatusid() {
        return transferstatusid;
    }

    public void setTransferstatusid(int transferstatusid) {
        this.transferstatusid = transferstatusid;
    }

    public String getTransferstatusdesc() {
        return transferstatusdesc;
    }

    public void setTransferstatusdesc(String transferstatusdesc) {
        this.transferstatusdesc = transferstatusdesc;
    }

    @Override
    public String toString() {
        return "TransferStatus{" +
                "transferstatusid=" + transferstatusid +
                ", transferstatusdesc='" + transferstatusdesc + '\'' +

                '}';
    }
}
