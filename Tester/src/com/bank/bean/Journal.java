package com.bank.bean;

import java.util.Date;

public class Journal extends BaseBean{
    
    private String flowNo;
    private Date flowDate;
    private String amount;
    private String debitAcc;
    private String creditAcc;
    private String errCode;
    private String state;
    public String getFlowNo() {
        return flowNo;
    }
    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
    }
    public Date getFlowDate() {
        return flowDate;
    }
    public void setFlowDate(Date flowDate) {
        this.flowDate = flowDate;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getDebitAcc() {
        return debitAcc;
    }
    public void setDebitAcc(String debitAcc) {
        this.debitAcc = debitAcc;
    }
    public String getCreditAcc() {
        return creditAcc;
    }
    public void setCreditAcc(String creditAcc) {
        this.creditAcc = creditAcc;
    }
    public String getErrCode() {
        return errCode;
    }
    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    
}
