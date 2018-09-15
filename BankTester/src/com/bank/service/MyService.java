package com.bank.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.bank.bean.Bill;
import com.bank.util.DBUtils;

public class MyService {
    
    
    private Connection online_conn = null;
    private PreparedStatement online_ps1 = null;
    private PreparedStatement online_ps2 = null;
    private PreparedStatement online_ps3 = null;
    private PreparedStatement online_ps4 = null;
    private PreparedStatement online_ps5 = null;
    private PreparedStatement online_ps6 = null;
    private PreparedStatement online_ps7 = null;
    
    private static String ONLINE_SQL1 = "insert into journal(flowno,flowdate,amount,debitacc,creditacc,state) values(?,?,?,?,?,?)";
    private static String ONLINE_SQL2 = "select currency,accnature,cuno from account where accno=? ";
    private static String ONLINE_SQL3 = "update journal set state=? where flowno=? and debitacc=? and creditacc=?";
    private static String ONLINE_SQL4 = "update journal set errcode=? where flowno=? and debitacc=? and creditacc=?";
    private static String ONLINE_SQL5 = "insert into bill(flowno,flowdate,accno,debitamount,credityield,note,cuno) values(?,?,?,?,?,?,?)";
    private static String ONLINE_SQL6 = "update account t set realtimeremain = realtimeremain-100 where cuno=?";
    private static String ONLINE_SQL7 = "update account t set realtimeremain = realtimeremain+100 where cuno=?";

    private static String BATCH_SQL1 = "select count(0),cuno from account where accno=? and currency=? and accnature=1 and realtimeremain >= ? group by cuno";
    private static String BATCH_SQL2 = "update account set realtimeremain=realtimeremain+? where accno=?";
    private static String BATCH_SQL3 = "select count(0),cuno from account where accno=? and currency=? and accnature=1 group by cuno";

    
    public MyService(){
        online_conn = DBUtils.getConn();
        try {
            online_ps1 = online_conn.prepareStatement(ONLINE_SQL1);
            online_ps2 = online_conn.prepareStatement(ONLINE_SQL2);
            online_ps3 = online_conn.prepareStatement(ONLINE_SQL3);
            online_ps4 = online_conn.prepareStatement(ONLINE_SQL4);
            online_ps5 = online_conn.prepareStatement(ONLINE_SQL5);
            online_ps6 = online_conn.prepareStatement(ONLINE_SQL6);
            online_ps7 = online_conn.prepareStatement(ONLINE_SQL7);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
//        (new MyService()).batchWithTransaction("6214680000000000", new BigDecimal(1000), "a", "CNY");
//        (new MyService()).onLineWithTransaction("6214680000000000", "6214680000000009", new BigDecimal(100));
//        System.out.println(Math.random());
        
     long beginTime = System.nanoTime();
      for (int i = 0; i < 50000000; i++) {
//          System.out.println(getFlowNo());
          getFlowNo();
        
    }
        long endTime = System.nanoTime();
        System.out.println("onLineWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s  " + ((endTime-beginTime)%1000000000) + "us");
//        
//        System.out.println(System.currentTimeMillis());
//        TimeUnit.SECONDS.sleep(3);
//        System.out.println(System.currentTimeMillis());
    }
    
    /**
     * 输入户名，查询这个户名下对应的账号的流水信息。
     * @param cuName
     * @return
     */
    public List<Bill> queryFlowByCuName(String cuName){
        
        if(StringUtils.isEmpty(cuName)){
            throw new RuntimeException("the input parameter is null.");
        }
        
        
        String sql = "select flowno,flowdate,accno,debitamount,credityield,b.cuno,abscode,note,reserve1,"
            +"reserve2,reserve3,reserve4 from bill b, customer a where b.cuno=a.cuno and a.cuname=?";
        Connection conn = DBUtils.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Bill> resList =new ArrayList<Bill>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, cuName);
            
            rs = ps.executeQuery();
            while(rs.next()){
                Bill bill = new Bill();
                bill.setAbsCode(rs.getString("abscode"));
                bill.setAccNo(rs.getString("accno"));
                bill.setCreditYield(rs.getBigDecimal("credityield"));
                bill.setCuNo(rs.getString("cuno"));
                bill.setDebitAmount(rs.getBigDecimal("debitamount"));
                bill.setFlowDate(rs.getDate("flowdate"));
                bill.setFlowNo(rs.getString("flowno"));
                bill.setNote(rs.getString("note"));
                bill.setReserve1(rs.getString("reserve1"));
                bill.setReserve2(rs.getString("reserve2"));
                bill.setReserve3(rs.getString("reserve3"));
                bill.setReserve4(rs.getString("reserve4"));
                resList.add(bill);
                
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            DBUtils.close(conn, rs, ps);
        }
        return resList;
    }
    
    /**
     * 联机有事务
     * @param debitAcc
     * @param creditAcc
     * @param amount
     * @throws SQLException 
     */
    public void onLineWithTransaction(String debitAcc, String creditAcc, BigDecimal amount) throws SQLException{
//        long beginTime = System.nanoTime();
          onLine(debitAcc, creditAcc, amount, true);
//        long endTime = System.nanoTime();
//        System.out.println("onLineWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");
    }
    
    /**
     * 联机无事务
     * @param debitAcc
     * @param creditAcc
     * @param amount
     * @throws SQLException 
     */
    public void onLineWithoutTransaction(String debitAcc, String creditAcc, BigDecimal amount) throws SQLException{
//        long beginTime = System.nanoTime();
        onLine(debitAcc, creditAcc, amount, false);
//        long endTime = System.nanoTime();
//        System.out.println("onLineWithoutTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");
        
    }
    
    /**
     * 批量有事务
     * @param debitAcc
     * @param amount
     * @param flag
     * @param currency
     * @throws SQLException 
     */
    public void batchWithTransaction(String debitAcc, BigDecimal amount,String flag, String currency) throws SQLException{
//        long beginTime = System.nanoTime();
        batch(debitAcc, amount, flag, currency, true);
//        long endTime = System.nanoTime();
//        System.out.println("batchWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");

    }
    
    /**
     * 批量无事务
     * @param debitAcc
     * @param amount
     * @param flag
     * @param currency
     * @throws SQLException 
     */
    public void batchWithoutTransaction(String debitAcc, BigDecimal amount,String flag, String currency) throws SQLException{
//        long beginTime = System.nanoTime();
        batch(debitAcc, amount, flag, currency, false);
//        long endTime = System.nanoTime();
//        System.out.println("batchWithoutTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");

    }        
    
    private void onLine(String debitAcc, String creditAcc, BigDecimal amount,boolean withTransaction) throws SQLException{
        
        if(StringUtils.isEmpty(debitAcc) || StringUtils.isEmpty(creditAcc) || null == amount){
            throw new RuntimeException("the input parameter is null.");
        }
        
        
        Date date = new Date(System.currentTimeMillis());
//        Connection conn = DBUtils.getConn();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
        String debitCuno = null;
        String creditCuno = null;
        //生成流水号并记录登记薄
        String flowNo = getFlowNo();
        try {
            
            //开启事务并扣款
            if(withTransaction){
                online_conn.setAutoCommit(false);
            }            
            
//            ps = conn.prepareStatement(ONLINE_SQL1);
            online_ps1.setString(1, flowNo);
            online_ps1.setDate(2, date);
            online_ps1.setBigDecimal(3, amount);
            online_ps1.setString(4, debitAcc);
            online_ps1.setString(5, creditAcc);
            online_ps1.setString(6, "0");
            
            online_ps1.executeUpdate();
//            ps.close();
            //检查借方账户信息
//            ps = conn.prepareStatement(ONLINE_SQL2);
            online_ps2.setString(1, debitAcc);
            
            String currency = null;
            String accnature = null;
            
            rs  = online_ps2.executeQuery();
            if(rs.next())
            {
                currency = rs.getString(1);
                accnature = rs.getString(2);
                debitCuno = rs.getString(3);
            }
//            ps.close();
            rs.close();
            
            if(StringUtils.isEmpty(currency) || StringUtils.isEmpty(accnature)||!"1".equals(accnature) || !"CNY".equals(currency))
            {
//                ps = conn.prepareStatement(ONLINE_SQL4);
                online_ps4.setString(1, "0000");
                online_ps4.setString(2, flowNo);
                online_ps4.setString(3, debitAcc);
                online_ps4.setString(4, creditAcc);                
                online_ps4.executeUpdate();                
                throw new SQLException("检查借方账户信息失败，debitAcc is " + debitAcc);
            }else {
//                ps = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "1"));
                online_ps3.setString(1, "1");
                online_ps3.setString(2, flowNo);
                online_ps3.setString(3, debitAcc);
                online_ps3.setString(4, creditAcc);                
                online_ps3.executeUpdate();
            }
//            ps.close();
            
            //检查贷方账户信息   
//            ps = conn.prepareStatement(ONLINE_SQL2);
            online_ps2.setString(1, creditAcc);
            
            currency = null;
            accnature = null;
            
            rs  = online_ps2.executeQuery();
            if(rs.next())
            {
                currency = rs.getString(1);
                accnature = rs.getString(2);
                creditCuno = rs.getString(3);
            }
//            ps.close();
            rs.close();
            
            if(StringUtils.isEmpty(currency) || StringUtils.isEmpty(accnature)||!"1".equals(accnature) || !"CNY".equals(currency))
            {
//                online_ps4 = conn.prepareStatement(ONLINE_SQL4);
                online_ps4.setString(1, "1111");
                online_ps4.setString(2, flowNo);
                online_ps4.setString(3, debitAcc);
                online_ps4.setString(4, creditAcc);                  
                online_ps4.executeUpdate();                
                throw new SQLException("检查贷方账户信息，debitAcc is " + debitAcc);
            }else {
//                ps = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "2"));
                online_ps3.setString(1, "2");
                online_ps3.setString(2, flowNo);
                online_ps3.setString(3, debitAcc);
                online_ps3.setString(4, creditAcc);                      
                online_ps3.executeUpdate();
            }    
//            ps.close();
            
            //登记流水表
//            ps = conn.prepareStatement(ONLINE_SQL5);
            online_ps5.setString(1, flowNo);
            online_ps5.setDate(2, date);
            online_ps5.setString(3, debitAcc);
            online_ps5.setBigDecimal(4, amount);
            online_ps5.setBigDecimal(5,new BigDecimal(0));
            online_ps5.setString(6, "xxx");
            online_ps5.setString(7, debitCuno);
            online_ps5.addBatch();
            
            online_ps5.setString(1, flowNo);
            online_ps5.setDate(2, date);
            online_ps5.setString(3, creditAcc);
            online_ps5.setBigDecimal(4, new BigDecimal(0));
            online_ps5.setBigDecimal(5,amount);
            online_ps5.setString(6, "xxx");
            online_ps5.setString(7, debitCuno);
            online_ps5.addBatch();
            online_ps5.executeBatch();            
//            ps.close();
//            ps = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "2"));
            online_ps3.setString(1, "3");
            online_ps3.setString(2, flowNo);
            online_ps3.setString(3, debitAcc);
            online_ps3.setString(4, creditAcc);              
            online_ps3.executeUpdate();    
            
            if(withTransaction){
            	online_conn.commit();
            }          
        }
        catch (Exception e) {
        	try {
        		if (withTransaction && null != online_conn && !online_conn.getAutoCommit()) {
        			online_conn.commit();
        		}
        		
        	}
        	catch (SQLException e1) {
        		e1.printStackTrace();
        		DBUtils.close(online_conn, rs, ps);
        		online_conn = DBUtils.getConn();
        		
        	}
//        	DBUtils.close(online_conn, rs, ps);
//        	online_conn = DBUtils.getConn();            
        	System.out.println("Exception was throw out, debitAcc:" + debitAcc);
        	e.printStackTrace();
        	throw new SQLException(e);
        }
        
        int step = 0;
        try{
//            ps.close();
            
            //开启事务并扣款
            if(withTransaction){
            	online_conn.setAutoCommit(false);
            }

//            ps = conn.prepareStatement(ONLINE_SQL6);
               online_ps6.setString(1, debitCuno);
               online_ps6.executeUpdate();
            
               step = 1;
//            ps.close();
            
//            ps.close();
               step = 2;
            
//            ps = conn.prepareStatement(ONLINE_SQL7);
              online_ps7.setString(1, creditCuno);
              online_ps7.executeUpdate();
//            online_ps7.close();
            
	      online_ps3.setString(1, "5");
	      online_ps3.setString(2, flowNo);
	      online_ps3.setString(3, debitAcc);
	      online_ps3.setString(4, creditAcc);              
  	      online_ps3.executeUpdate();              	

            if(withTransaction){
                online_conn.commit();
            }
        }
        catch (Exception e) {
        	try {
        		
        		if (withTransaction && null != online_conn && !online_conn.getAutoCommit()) {
        			online_conn.rollback();
        			if(step == 1){
//            ps = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "3"));
        				online_ps3.setString(1, "4");
        				online_ps3.setString(2, flowNo);
        				online_ps3.setString(3, debitAcc);
        				online_ps3.setString(4, creditAcc);              
        				online_ps3.executeUpdate();             
        				
        			}
        			else if (step == 1){
//            ps = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "4"));
        				online_ps3.setString(1, "5");
        				online_ps3.setString(2, flowNo);
        				online_ps3.setString(3, debitAcc);
        				online_ps3.setString(4, creditAcc);              
        				online_ps3.executeUpdate();    
        				
        			}
        			
        			
        		}
        	}
        	catch (SQLException e1) {
        		e1.printStackTrace();
        		DBUtils.close(online_conn, rs, ps);
        		online_conn = DBUtils.getConn();
        		
        	}
//        	DBUtils.close(online_conn, rs, ps);
//        	online_conn = DBUtils.getConn();            
        	e.printStackTrace();
        	throw new SQLException(e);
        }

           
    }
        finally{
//            DBUtils.close(conn, rs, ps);
            DBUtils.close(null, rs, null);
            online_conn.setAutoCommit(true);
        }
        
    }
    
    private void batch(String debitAcc, BigDecimal amount,String flag, String currency, boolean withTransaction ) throws SQLException{
        if(StringUtils.isEmpty(debitAcc) || null == amount|| StringUtils.isEmpty(flag) || StringUtils.isEmpty(currency) ){
            throw new RuntimeException("the input parameter is null.");
        }
        Connection conn = DBUtils.getConn();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Date date = new Date(System.currentTimeMillis());        
        String flowNo = getFlowNo();
        
        try {
            //检查借方账号信息
            ps = conn.prepareStatement(BATCH_SQL1);
            ps.setString(1, debitAcc);
            ps.setString(2, currency);
            ps.setBigDecimal(3, amount);
            rs = ps.executeQuery();
            int res = 0;
            String cuno = "";
            if(rs.next())
            {
                res = rs.getInt(1);
                cuno = rs.getString(2);
            }
            if(res == 0){
                throw new RuntimeException("检查借方账号信息失败。");
            }
            rs.close();
            
            if(withTransaction){
                conn.setAutoCommit(false);
            }
            //扣借方账号的账
            ps = conn.prepareStatement(BATCH_SQL2);
            ps.setBigDecimal(1,new BigDecimal(0).subtract(amount));
            ps.setString(2, debitAcc);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement(ONLINE_SQL5);
            ps.setString(1, flowNo);
            ps.setDate(2, date);
            ps.setString(3, debitAcc);
            ps.setBigDecimal(4, amount);
            ps.setBigDecimal(5,amount);
            ps.setString(6, "批量");
            ps.setString(7, cuno);
            ps.executeUpdate();   
            ps.close();
            
            PreparedStatement ps1 = null;
            PreparedStatement ps2= null;
            PreparedStatement ps3 = null;
            ps1 = conn.prepareStatement(BATCH_SQL2);
            ps2 = conn.prepareStatement(ONLINE_SQL5);
            ps3 = conn.prepareStatement(ONLINE_SQL3.replace("STATE", "2"));
            
            String debitAcc1 = "";
            for( int i = 999, A =1, b = 1;b < i; b++,A++ )
            {
                ps1.clearParameters();
                ps2.clearParameters();
                ps3.clearParameters();
//                检查账号A的状态                
                debitAcc1 = String.valueOf(Long.valueOf(debitAcc) + b);
                ps = conn.prepareStatement(BATCH_SQL3);
                ps.setString(1, debitAcc1);
                ps.setString(2, currency);
                rs = ps.executeQuery();
                res = 0;
                if(rs.next())
                {
                    res = rs.getInt(1);
                    cuno = rs.getString(2);
                }
                if(res == 0){
                    throw new RuntimeException("检查账号A[" + debitAcc1 + "]的状态失败。");
                }    
                rs.close();
                
//                账号A入账：余额=余额+发生额A
                ps1.setBigDecimal(1,new BigDecimal(A));
                ps1.setString(2, debitAcc1);
                ps1.executeUpdate(); 
//                记流水表
                ps2.setString(1, getFlowNo());
                ps2.setDate(2, date);
                ps2.setString(3, debitAcc1);
                ps2.setBigDecimal(4, amount);
                ps2.setBigDecimal(5,amount);
                ps2.setString(6, "xxx");
                ps2.setString(7, cuno);
                ps2.executeUpdate();
            }
            if(withTransaction){
                conn.commit();
            }            
            
        }
        catch (SQLException e) {
            try {
                if (withTransaction && null != conn && !conn.getAutoCommit()) {
                    conn.rollback();
                }
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
//            e.printStackTrace();
            throw e;
            
        }
        finally{
            DBUtils.close(conn, rs, ps);
        }
    }

    private static String getFlowNo(){
//        return UUID.randomUUID().toString();
        return String.valueOf(System.currentTimeMillis()%1000000) + (int)(Math.random()*1000) + (int)(Math.random()*1000) + (int)(Math.random()*1000) + (int)(Math.random()*1000);
    }
}
