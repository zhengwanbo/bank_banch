package com.bank.bean;

import java.security.Timestamp;

public class Customer extends BaseBean{

    private String cuNo;
    private String cuName;
    private String papType;
    private String papNo;
    private String phoneNo;
    private String addr;
    private String cuState;
    private Timestamp modifyTime;

    public String getCuNo() {
        return cuNo;
    }
    public void setCuNo(String cuNo) {
        this.cuNo = cuNo;
    }
    public String getCuName() {
        return cuName;
    }
    public void setCuName(String cuName) {
        this.cuName = cuName;
    }
    public String getPapType() {
        return papType;
    }
    public void setPapType(String papType) {
        this.papType = papType;
    }
    public String getPapNo() {
        return papNo;
    }
    public void setPapNo(String papNo) {
        this.papNo = papNo;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getCuState() {
        return cuState;
    }
    public void setCuState(String cuState) {
        this.cuState = cuState;
    }
    public Timestamp getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public static void main(String[] args) {
        Customer cus = new Customer();
        System.out.println(cus);
    }
}
