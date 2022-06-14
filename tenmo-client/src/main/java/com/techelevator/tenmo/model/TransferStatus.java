package com.techelevator.tenmo.model;

public class TransferStatus {
    private int transferstatusid;
    private String transferstatusdesc;

    public TransferStatus(int id, String status) {
        this.transferstatusid = id;
        this.transferstatusdesc = status;
    }

    public TransferStatus() {
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
}
