package com.bank.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.util.ResourceUtils;

import com.bank.domain.Config;


public class ParseConfig {
    
    public static Config parse() throws Exception{
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
            cfg = JaxbMapper.fromXml(str.toString(), Config.class);
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
        return cfg;
        
    }
    

}
