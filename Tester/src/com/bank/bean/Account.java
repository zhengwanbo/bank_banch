package com.bank.bean;

import java.math.BigDecimal;

public class Account extends BaseBean{

    private String accNo;
    private String accState;
    private BigDecimal realtimeRemain;
    private String currency;
    private BigDecimal rate;
    private String accNature;
    private String cuNo;
    public String getAccNo() {
        return accNo;
    }
    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }
    public String getAccState() {
        return accState;
    }
    public void setAccState(String accState) {
        this.accState = accState;
    }
    public BigDecimal getRealtimeRemain() {
        return realtimeRemain;
    }
    public void setRealtimeRemain(BigDecimal realtimeRemain) {
        this.realtimeRemain = realtimeRemain;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getAccNature() {
        return accNature;
    }
    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }
    public String getCuNo() {
        return cuNo;
    }
    public void setCuNo(String cuNo) {
        this.cuNo = cuNo;
    }
    
}
