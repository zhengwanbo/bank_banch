package com.bank.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.bank.domain.Column;
import com.bank.domain.Config;
import com.bank.domain.Table;
import com.bank.util.DBUtils;
import com.bank.util.ParseConfig;

public class InitDataService {
    
    public static void main(String[] args) throws SQLException {
        initData();
    }
    
    public static void initData() throws SQLException {
//        System.out.println("begin to init data");
        Config cfg = null;
        try {
            cfg = ParseConfig.parse();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Connection conn = DBUtils.getConn(false);
        PreparedStatement ps = null;
        try {

            if (cfg.getCommonConfig().isCleanDataBeforeInit()) {
                for (Table tlb : cfg.getTables()) {
                    if(!tlb.isInit()){
                        continue;
                    }
                    ps = conn.prepareStatement("truncate table " + tlb.getName());
                    ps.executeUpdate();
                }
            }
            conn.commit();

            for (Table tlb : cfg.getTables()) {
                if(!tlb.isInit()){
                    continue;
                }
                long tableBegin = System.nanoTime();
                String preInsertSql = "insert into ";
                preInsertSql += tlb.getName() + "(";
                for (Column col : tlb.getList()) {
                    preInsertSql += col.getName() + ",";
                }
                preInsertSql = preInsertSql.substring(0, preInsertSql.length() - 1) + " ) values(";

                for (int i = 0; i < tlb.getList().size() - 1; i++) {
                    preInsertSql += "?,";
                }
                preInsertSql += "?)";
                ps = conn.prepareStatement(preInsertSql);

                List<Map> list = new ArrayList<Map>();
                int limit = tlb.getInitCapacity() % 5000 == 0 ? (tlb.getInitCapacity() / 5000)
                                : (tlb.getInitCapacity() / 5000 + 1);
                String key = "";
                Map currentValue = new HashMap();

                for (int j = 0; j < limit; j++) {
                    long beginNs = System.nanoTime();
                    for (int i = 0; i < 5000 && j * 5000 + i < tlb.getInitCapacity(); i++) {
                        Map map = null;

                        for (int k = 0; k < tlb.getRepeatFrequency(); k++) {

                            map = new LinkedHashMap();
                            for (Column clm : tlb.getList()) {
                                String type = clm.getType();
                                // type = "int".equalsIgnoreCase(type) ? "int" : "string";
                                type = type.toLowerCase().trim();
                                if (type.equals("int")) {
                                    if (clm.isKey()) {
                                        int value = 0;
                                        if (currentValue.containsKey("__KEY__")) {
                                            value = (Integer) currentValue.get("__KEY__");
                                        }
                                        else {
                                            value = clm.getStartVaule();
                                        }
                                        currentValue.put("__KEY__", value);
                                        map.put(clm.getName(), value);
                                    }
                                    else {
                                        if (null != clm.getValueRange() && !"".equals(clm.getValueRange())) {
                                            String[] values = clm.getValueRange().split("\\|");
                                            int index = (int) (Math.random() * values.length);
                                            map.put(clm.getName(), values[index]);

                                        }
                                        else {

                                            int value = 0;
                                            if (currentValue.containsKey(clm.getName())) {
                                                value = (Integer) currentValue.get(clm.getName()) + 1;
                                            }
                                            else {
                                                value = clm.getStartVaule();
                                            }
                                            currentValue.put(clm.getName(), value);
                                            map.put(clm.getName(), clm.getStartVaule());
                                        }
                                    }
                                }
                                else if (type.equals("string")) {
                                    String aa = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                                    int val = 0;
                                    if (currentValue.containsKey(clm.getName())) {
                                        val = (Integer) currentValue.get(clm.getName()) + 1;
                                    }
                                    currentValue.put(clm.getName(), val);
                                    String value = "";
                                    if (clm.getPreStr().length() < clm.getLength()) {
                                        value = clm.getPreStr()
                                                        + aa
                                                                        .substring(clm.getPreStr().length()
                                                                                        + String.valueOf(val).length(),
                                                                                        clm.getLength())
                                                        + String.valueOf(val);
                                    }
                                    else {
                                        value = clm.getPreStr().substring(0, clm.getLength());
                                    }
                                    map.put(clm.getName(), value);
                                }
                                else if (type.equals("decimal")) {
                                }

                                clm.setStartVaule(clm.getStartVaule() + 1);
                            }
                            list.add(map);
                        }
                        if (currentValue.containsKey("__KEY__")) {
                            currentValue.put("__KEY__", (Integer) currentValue.get("__KEY__") + 1);
                        }
                    }
                    for (Map<String, Object> map : list) {
                        int i = 1;
                        for (String str : map.keySet()) {
                            ps.setString(i++, String.valueOf(map.get(str)));
                        }
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    ps.clearBatch();
                    ps.clearParameters();
                    conn.commit();
                    long endNs = System.nanoTime();
                    System.out.println("batch insert: " + tlb.getName()+" 5000rows. cost " +(endNs - beginNs) + "us");
                    list.clear();
                }
                currentValue.clear();
                long endTable = System.nanoTime();
                System.out.println("init table[" + tlb.getName() + "],cost:" + ((endTable - tableBegin) / 1000000000)
                                + "s.");
            }
            System.out.println("End to init data");
        }
        finally {
            DBUtils.close(conn, null, ps);
        }

    }

}
