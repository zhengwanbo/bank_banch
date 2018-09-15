package com.bank.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Bill extends BaseBean{

    private String flowNo;
    private Date flowDate;
    private String accNo;
    private BigDecimal debitAmount;
    private BigDecimal creditYield;
    private String cuNo;
    private String absCode;
    private String note;
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
    public String getAccNo() {
        return accNo;
    }
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    public BigDecimal getDebitAmount() {
        return debitAmount;
    }
    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }
    public BigDecimal getCreditYield() {
        return creditYield;
    }
    public void setCreditYield(BigDecimal creditYield) {
        this.creditYield = creditYield;
    }
    public String getCuNo() {
        return cuNo;
    }
    public void setCuNo(String cuNo) {
        this.cuNo = cuNo;
    }
    public String getAbsCode() {
        return absCode;
    }
    public void setAbsCode(String absCode) {
        this.absCode = absCode;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    
    
}
