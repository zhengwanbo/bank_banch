package com.bank.domain;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Column {
    
    private String name;
    
    private String type;
    
    private String valueRange;
    
    private int startVaule;
    
    private boolean key;
    
    private String preStr;
    
    private int length;

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @XmlAttribute
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if(length >500){
            length=20;
        }
        this.length = length;
    }

    @XmlAttribute
    public String getPreStr() {
        return preStr;
    }

    public void setPreStr(String preStr) {
        this.preStr = preStr;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute
    public String getValueRange() {
        return valueRange;
    }

    public void setValueRange(String valueRange) {
        this.valueRange = valueRange;
    }

    @XmlAttribute
    public int getStartVaule() {
        return startVaule;
    }

    public void setStartVaule(int startVaule) {
        this.startVaule = startVaule;
    }

    @XmlAttribute
    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
