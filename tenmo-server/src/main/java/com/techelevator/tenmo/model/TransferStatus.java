package com.techelevator.tenmo.model;

public class TransferStatus {
    private int transferstatusid;
    private String transferstatusdesc;

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
}
