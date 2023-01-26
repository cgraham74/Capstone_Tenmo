package com.techelevator.tenmo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "transfer_type")
public class TransferType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_type_id")
    private int transfertypeid;

    @Column(name = "transfer_type_desc")
    private String transfertypedesc;


    public TransferType(int transfertypeid, String transfertypedesc) {
        this.transfertypeid = transfertypeid;
        this.transfertypedesc = transfertypedesc;
    }

    @OneToMany()
    @JoinColumn(name = "transfer_type_id", insertable = false, updatable = false)
    private List<Transfer> transfer;

    public TransferType() {
    }

    public int getTransfertypeid() {
        return transfertypeid;
    }

    public void setTransfertypeid(int transfertypeid) {
        this.transfertypeid = transfertypeid;
    }

    public String getTransfertypedesc() {
        return transfertypedesc;
    }

    public void setTransfertypedesc(String transfertypedesc) {
        this.transfertypedesc = transfertypedesc;
    }

    @Override
    public String toString() {
        return "TransferType{" +
                "transfertypeid=" + transfertypeid +
                ", transfertypedesc='" + transfertypedesc + '\'' +

                '}';
    }
}
