package com.bank.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommonConfig {
    
    private boolean cleanDataBeforeInit;
    
    public boolean isCleanDataBeforeInit() {
        return cleanDataBeforeInit;
    }

    public void setCleanDataBeforeInit(boolean cleanDataBeforeInit) {
        this.cleanDataBeforeInit = cleanDataBeforeInit;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
