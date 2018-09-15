package com.bank.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseBean {
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    
    public String getReserve1() {
        return reserve1;
    }
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }
    public String getReserve2() {
        return reserve2;
    }
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }
    public String getReserve3() {
        return reserve3;
    }
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
    public String getReserve4() {
        return reserve4;
    }
    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
