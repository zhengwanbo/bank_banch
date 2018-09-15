package com.bank.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Table {
    
    private String name;
    
    private int repeatFrequency;
    
    private int initCapacity;
    
    private boolean init;
    
    private List<Column> list = new ArrayList<Column>();

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @XmlAttribute
    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public void setList(List<Column> list) {
        this.list = list;
    }

    @XmlAttribute
    public int getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(int repeatFrequency) {
        if(repeatFrequency <1){
            repeatFrequency = 1;
        }
        if(repeatFrequency >10)
        {
            repeatFrequency = 10;
        }
        
        this.repeatFrequency = repeatFrequency;
    }

    @XmlAttribute
    public int getInitCapacity() {
        return initCapacity;
    }

    public void setInitCapacity(int initCapacity) {
        this.initCapacity = initCapacity;
    }

    @XmlElement(name = "column")    
    public List<Column> getList() {
        return list;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
