package com.techelevator.tenmo.model;

public class TransferType {
    private int transfertypeid;
    private String transfertypedesc;


    public TransferType(int transfertypeid, String transfertypedesc) {
        this.transfertypeid = transfertypeid;
        this.transfertypedesc = transfertypedesc;
    }

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
}
