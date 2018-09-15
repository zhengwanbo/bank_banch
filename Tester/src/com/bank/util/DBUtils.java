package com.bank.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtils {
    
    private static ComboPooledDataSource ds = null;
    
    static{
        ds = new ComboPooledDataSource("goldenDbConnection");
    }
    
    public static void shutdownPool(){
        ds.close();
    }
    
    public static Connection getConn(boolean autoCommit)
    {
        try {
            Connection conn = ds.getConnection();
            conn.setAutoCommit(autoCommit);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public static Connection getConn()
    {
        return getConn(true);
    }
    
    public static void close(Connection conn, ResultSet rs, Statement ps)
    {
     
        try {
            if(null != rs){
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if(null != ps){
                ps.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            if(null != conn && !conn.isClosed()){
                conn.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
