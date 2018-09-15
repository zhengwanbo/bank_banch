package com.bank.domain;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.ResourceUtils;

import com.bank.util.JaxbMapper;

@XmlRootElement(name = "root")
public class Config {
    
    
    private CommonConfig commonConfig = new CommonConfig();
    
    private List<Table> tables = new ArrayList<Table>();
    

    @XmlElement(name="commonConfig")
    public CommonConfig getCommonConfig() {
        return commonConfig;
    }
    
    @XmlElementWrapper
    @XmlElement(name = "table")
    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public void setCommonConfig(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }
    
    public static void main(String[] args) {
        Config cfg = new Config();

        FileInputStream fs = null;
        StringBuffer str = new StringBuffer();
        try {
            fs = new FileInputStream(ResourceUtils.getFile("classpath:config.xml"));
            InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                str.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (null != fs) {
                try {
                    fs.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        try {
            cfg = JaxbMapper.fromXml(str.toString(), Config.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
System.out.println(cfg);    
    }
    
    public static void main1(String[] args) {
        Config cfg = new Config();

        Table tbl = new Table();
        tbl.setInitCapacity(10);
        tbl.setName("name");
        tbl.setInit(true);
        tbl.setRepeatFrequency(10);
        Column cln = new Column();
        cln.setKey(true);
        cln.setName("cln1");
        cln.setStartVaule(1000);
        cln.setType("int");
        cln.setValueRange("0|1");
        tbl.getList().add(cln);
        
        cfg.getTables().add(tbl);
        System.out.println(JaxbMapper.toXmlWithFormat(cfg));
        
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
