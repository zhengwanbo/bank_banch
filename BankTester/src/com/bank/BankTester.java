package com.bank;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import com.bank.bean.Bill;
import com.bank.service.InitDataService;
import com.bank.service.MyService;
import com.bank.util.DBUtils;

public class BankTester {

    static {
        Connection conn = DBUtils.getConn();
        DBUtils.close(conn, null, null);
    }

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) {
        try {

         Options JDUL = new Options();
         JDUL.addOption("init"   ,true, "init data");
         JDUL.addOption("case" , true,  "select the case to run");
         JDUL.addOption("threads" ,true,  "the number of thread");
         JDUL.addOption("capacity", true, "the capacity of test");
         //JDUL.addOption("account", true, "the number of account");
         JDUL.addOption("start_c", true, "the start of account");
         JDUL.addOption("end_c", true, "the end of account");
         CommandLineParser parser = new DefaultParser();
         CommandLine cl = parser.parse(JDUL, args);
         boolean isInit = Boolean.valueOf(cl.getOptionValue("init"));
         char  runCase = cl.getOptionValue("case").toCharArray()[0];
         int threads = Integer.parseInt(cl.getOptionValue("threads"));
         int capacity = Integer.parseInt(cl.getOptionValue("capacity"));
         // int accountNum = Integer.parseInt(cl.getOptionValue("account"));
         int start_c = Integer.parseInt(cl.getOptionValue("start_c"));
         int end_c = Integer.parseInt(cl.getOptionValue("end_c"));


//         StringBuffer tipInfo = new StringBuffer("begin to run test tool, tool functions as below: \n");
//         tipInfo.append("1：initdata\n");
//         tipInfo.append("2：single test 'online with transaction'\n");
//         tipInfo.append("3：concurrent test 'online with transaction'\n");
//         tipInfo.append("4：single test 'online without transaction'\n");
//         tipInfo.append("5：concurrent test 'online without transaction'\n");
//         tipInfo.append("6：test 'batch with transaction'\n");
//         tipInfo.append("7：test 'batch without trancaction'\n");
//         tipInfo.append("8：test 'query bill by customer name'\n");
//         tipInfo.append("9：concurrent test 'batch with transaction'\n");
//         tipInfo.append("0：concurrent test 'batch without trancaction'\n");
//         tipInfo.append("a：tidb-concurrent test 'online with transaction new'\n");
//         tipInfo.append("q：exit tool\n");

         ExecutorService executor = null;

         while(true){
//             System.out.println(tipInfo.toString());
//            showTips();
//            char res = getInput("please choose:\n");
            boolean exit = true;
            if(isInit == true) {
                runCase = '1';
                exit = false;
                isInit = false;
             }
            switch (runCase) {
                case '1':
                    System.out.println("'initdata' is chose.");
                    //System.out.print("please confirm that node 'tables' configed in config.xml is right. press 'y|Y' to continue,others to back:");
                    //char c = getInputChar();
                   // if(c == 'y' || c == 'Y'){
                        System.out.println("begin to init data.");
                        try {
                            InitDataService.main(null);
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                            System.out.println("Exceptions was throwed out, please check.");
                        }
                    //}
                    runCase = cl.getOptionValue("case").toCharArray()[0];
                    break;
                case '2':
                    System.out.println("single test 'online with transaction' is chose.");
                    System.out.println("please input the parameters,such as '6214680000000000 6214680000000009 100', split with space:");
                    String[] params = getInputString().split(" ");
                    if(params.length < 3)
                    {
                        System.out.println("num of input parameters should be 3");
                        break;
                    }
                    try {
                        String srcAcc = params[0];
                        String destAcc = params[1];
                        BigDecimal am = new BigDecimal(params[2]);
                        long beginTime = System.nanoTime();
                        (new MyService()).onLineWithoutTransaction(srcAcc, destAcc, am);
                        long endTime = System.nanoTime();
                        System.out.println("onLineWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");

                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Exceptions was throwed out, please check.");
                    }
                    break;
                case '3':
                    System.out.println("concurrent test 'online with transaction' is chose, the input param will be loaded from param.txt.");
                    System.out.println("please input thread num:");
                    int threadNum = getInputInt();

                    final List<String> paramList = loadParamter();
                    executor = Executors.newFixedThreadPool(threadNum);

                    long beginTime = System.nanoTime();
                    for(final String param : paramList){
                        executor.submit(new Runnable() {

                            @Override public void run() {
                                try {
                                    String[] params = param.split("\\|");
                                    String srcAcc = params[0];
                                    String destAcc = params[1];
                                    BigDecimal am = new BigDecimal(params[2]);
                                    (new MyService()).onLineWithTransaction(srcAcc, destAcc, am);
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    executor.shutdown();


                    long endTime = System.nanoTime();
                    while (true) {
                        if (executor.isTerminated()) {
                            break;
                        }
                        try {
                            Thread.sleep(500);
                            endTime = System.nanoTime();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("concurrent onLineWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s" + ((endTime-beginTime)%1000000000) + "us");
                    break;
                case '4':
                    System.out.println("single test 'online without transaction' is chose.");
                    System.out.println("please input the parameters,such as '6214680000000000 6214680000000009 100', split with space:");
                    params = getInputString().split(" ");
                    if(params.length < 3)
                    {
                        System.out.println("num of input parameters should be 3");
                        break;
                    }
                    try {
                        String srcAcc = params[0];
                        String destAcc = params[1];
                        BigDecimal am = new BigDecimal(params[2]);
                        long beginTime1 = System.nanoTime();
                        (new MyService()).onLineWithoutTransaction(srcAcc, destAcc, am);
                        long endTime1 = System.nanoTime();
                        System.out.println("onLineWithoutTransaction cost:"+((endTime1-beginTime1)/1000000000) + "s" + ((endTime1-beginTime1)%1000000000) + "us");

                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Exceptions was threw out, please check.");
                    }

                    break;
                case '5':
                    System.out.println("concurrent test 'online without transaction' is chose, the input param will be loaded from param.txt.");
                    System.out.println("please input thread num:");
                    int threadNum1 = getInputInt();

                    final List<String> paramList1 = loadParamter();
                    executor = Executors.newFixedThreadPool(threadNum1);

                    long beginTime2 = System.nanoTime();
                    for(final String param : paramList1){
                        executor.submit(new Runnable() {

                            @Override public void run() {
                                try {
                                    String[] params = param.split("\\|");
                                    String srcAcc = params[0];
                                    String destAcc = params[1];
                                    BigDecimal am = new BigDecimal(params[2]);
                                    (new MyService()).onLineWithoutTransaction(srcAcc, destAcc, am);
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    executor.shutdown();
                    long endTime2 = System.nanoTime();
                    while (true) {
                        if (executor.isTerminated()) {
                            break;
                        }
                        try {
                            Thread.sleep(500);
                            endTime2 = System.nanoTime();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("concurrent onLineWithoutTransaction cost:"+((endTime2-beginTime2)/1000000000) + "s" + ((endTime2-beginTime2)%1000000000) + "us");
                    break;
                case '6':
                    System.out.println("test 'batch with transaction'.");
                    System.out.println("please input the parameters,such as '6214680000000000 1000 a CNY', split with space:");
                    params = getInputString().split(" ");
                    if(params.length < 4)
                    {
                        System.out.println("num of input parameters should be 4");
                        break;
                    }
                    try {
                        String srcAcc = params[0];
                        BigDecimal am = new BigDecimal(params[1]);
                        String flag = params[2];
                        String currency = params[3];
                        long beginTime1 = System.nanoTime();
                        (new MyService()).batchWithTransaction(srcAcc, am, flag, currency);
                        long endTime1 = System.nanoTime();
                        System.out.println("batchWithTransaction cost:"+((endTime1-beginTime1)/1000000000) + "s" + ((endTime1-beginTime1)%1000000000) + "us");

                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Exceptions was threw out, please check.");
                    }

                    break;
                case '7':
                    System.out.println("test 'batch without transaction'.");
                    System.out.println("please input the parameters,such as '6214680000000000 1000 a CNY', split with space:");
                    params = getInputString().split(" ");
                    if(params.length < 4)
                    {
                        System.out.println("num of input parameters should be 4");
                        break;
                    }
                    try {
                        String srcAcc = params[0];
                        BigDecimal am = new BigDecimal(params[1]);
                        String flag = params[2];
                        String currency = params[3];
                        long beginTime1 = System.nanoTime();
                        (new MyService()).batchWithoutTransaction(srcAcc, am, flag, currency);
                        long endTime1 = System.nanoTime();
                        System.out.println("batchWithoutTransaction cost:"+((endTime1-beginTime1)/1000000000) + "s" + ((endTime1-beginTime1)%1000000000) + "us");

                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("Exceptions was threw out, please check.");
                    }

                    break;
                case '8':
                    System.out.println("'query bill by customer name' is chose,please input customer name:");
                    String cuName = getInputString();
                    List<Bill> resList = (new MyService()).queryFlowByCuName(cuName.trim());
                    for(Bill bill : resList){
                        System.out.println(bill);
                    }
                    break;
                case '9':
                    System.out.println("concurrent test 'batch with transaction' is chose, the input param will be loaded from param.txt.");
                    System.out.println("please input thread num:");
                    int threadNum2 = getInputInt();

                    final List<String> paramList2 = loadParamter();
                    executor = Executors.newFixedThreadPool(threadNum2);

                    long beginTime3 = System.nanoTime();
                    for(final String param : paramList2){
                        executor.submit(new Runnable() {

                            @Override public void run() {
                                try {
                                    String[] params = param.split("\\|");
                                    String srcAcc = params[0];
                                    BigDecimal am = new BigDecimal(params[1]);
                                    String flag = params[2];
                                    String currency = params[3];
                                    (new MyService()).batchWithTransaction(srcAcc, am, flag, currency);
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    executor.shutdown();
                    long endTime3 = System.nanoTime();
                    while (true) {
                        if (executor.isTerminated()) {
                            break;
                        }
                        try {
                            Thread.sleep(500);
                            endTime3 = System.nanoTime();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("concurrent batchWithTransaction cost:"+((endTime3-beginTime3)/1000000000) + "s" + ((endTime3-beginTime3)%1000000000) + "us");
                    break;
                case '0':
                    System.out.println("concurrent test 'batch with transaction' is chose, the input param will be loaded from param.txt.");
                    System.out.println("please input thread num:");
                    int threadNum3 = getInputInt();

                    final List<String> paramList3 = loadParamter();
                    executor = Executors.newFixedThreadPool(threadNum3);

                    long beginTime4 = System.nanoTime();
                    for(final String param : paramList3){
                        executor.submit(new Runnable() {

                            @Override public void run() {
                                try {
                                    String[] params = param.split("\\|");
                                    String srcAcc = params[0];
                                    BigDecimal am = new BigDecimal(params[1]);
                                    String flag = params[2];
                                    String currency = params[3];
                                    (new MyService()).batchWithoutTransaction(srcAcc, am, flag, currency);
                                }
                                catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    executor.shutdown();
                    long endTime4 = System.nanoTime();
                    while (true) {
                        if (executor.isTerminated()) {
                            break;
                        }
                        try {
                            Thread.sleep(500);
                            endTime3 = System.nanoTime();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("concurrent batchWithoutTransaction cost:"+((endTime4-beginTime4)/1000000000) + "s" + ((endTime4-beginTime4)%1000000000) + "us");
                    break;
                case 'a':
//                    System.out.println("concurrent test 'online with transaction new' is chose, parameter definitions as below.");
//                    System.out.println("Param 1: src account start");
//                    System.out.println("Param 2: dest account start");
//                    System.out.println("Param 3: test capacity");
//                    System.out.println("Param 4: thread nums");
//                    System.out.println("Param 5: test group nums");
//                    System.out.println("Param 6: group index");
//                    System.out.println("please input the parameters,such as '6214680000000000 6214680010000000 10000000 100 2 1':");
//                    params = getInputString().split(" ");
//                    if(params.length < 6)
//                    {
//                        System.out.println("num of input parameters should be 6");
//                        break;
//                    }
//                    System.out.println("Please input starttime,such as '15:40:30':");
//                    final String startTime = getInputString();


//                    long srcAcc = Long.valueOf(params[0]);
//                    long destAcc = Long.valueOf(params[1]);
//                    int capacity = Integer.valueOf(params[2]);
//                    int threads = Integer.valueOf(params[3]);
//                    final int instanceNum = Integer.valueOf(params[4]);
//                    int instanceId = Integer.valueOf(params[5]);


//                    final long srcAcc1 = srcAcc + capacity * (instanceId - 1) / instanceNum;
//                    final long destAcc1 = destAcc + capacity * (instanceId - 1) / instanceNum;

                    beginTime = System.nanoTime();
                    final int cellNum = capacity/threads;
                    executor = Executors.newFixedThreadPool(threads);
                    int half = (end_c - start_c) / 2;
                    int peer = half/threads;
                    System.out.println("capacity: " + capacity + " cellNum: " + cellNum + " peer: " + peer + " threads: " + threads );

                    for (int i = 0; i < threads; i++) {
                        int x = i;
                        executor.submit(new Runnable() {
                            Random random = new Random();
                            @Override
                            public void run() {
                                MyService service = new MyService();
                                BigDecimal am = new BigDecimal(1);
                                long runTime = System.nanoTime();
                                
                                for (int j = 0; j <  cellNum; j++) {
                                    long randomnum1 = 6214680000000000L+(long)(random.nextInt(peer) + x*peer);
                                    try {
                                        service.onLineWithTransaction(String
                                                .valueOf(randomnum1 + start_c), String
                                                .valueOf(randomnum1 + half + start_c), am);
                                    } catch (SQLException e) {
						                 e.printStackTrace();
                                    }

				                    if ( x == 1  && (j % 100 == 0 || j == cellNum - 1)) {
                    		            System.out.println( "cellNum: " + (cellNum-j) + " runNUM: " + j + " cost:"+((System.nanoTime()- runTime)/1000000000) + "s" );   
                    		             runTime = System.nanoTime();
				                    }
				                 }
                            }
                        });
                    }
                    
                    executor.shutdown();
                    endTime = System.nanoTime();
                    while (true) {
                        if (executor.isTerminated()) {
                            break;
                        }
                        try {
                            Thread.sleep(500);
                            endTime = System.nanoTime();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                   // System.out.println("capacity:" + capacity + " concurrent onLineWithTransaction cost:"+((endTime-beginTime)/1000000000) + "s " + ((endTime-beginTime)%1000000000) + "us");
                    System.out.println("capacity:" + capacity + " cost duraction:"+((endTime-beginTime)/1000000000) + "s " + " TPS:" + capacity/((endTime-beginTime)/1000000000));
                    break;
                case 'q':
                    exit = true;
                    break;
                default:
                    break;
            }
            if(exit)
            {
                break;
            }
         }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            DBUtils.shutdownPool();
            System.exit(0);
        }


    }

    private static void showTips(){

    }
    
    private static String getInputString(){
        String res = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            res = reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static int getInputInt(){
        String res = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            res = reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (res.trim().length() < 1) {
            System.out.print("wrong input, please input again:");
            return getInputInt();
        }
        int r = 0 ;
        try {
            r = Integer.valueOf(res);
        }
        catch (NumberFormatException e) {
            System.out.print("wrong input, please input again:");
            return getInputInt();
        }

        if(r < 1){
            System.out.print("wrong input, please input again:");
            return getInputInt();
        }
        return r;
    }

    private static char getInputChar(){
        String res = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            res = reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(res.trim().length()!=1){
            System.out.print("wrong input, please input again:");
           return getInputChar();
        }
        return res.trim().charAt(0);
    }

    private static char getInput(String tip) {
        System.out.println(tip);
        String res = "";
        char c = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            res = reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(res.trim().length()!=1){
            System.out.print("wrong input,");
            return  getInput(tip);
        }
        c = res.trim().charAt(0);
        if ((c > '9' || c < '0') && !(c == 'q' || c == 'a')) {
            System.out.print("wrong input,");
            return  getInput(tip);

        }

        return c;
    }


    private static List<String> loadParamter(){
        List<String> paramList = new ArrayList<String>();
        File paramFile = new File(BankTester.class.getClassLoader()
                        .getResource("").getPath()+"param.txt");
        if (!paramFile.exists()) {
            throw new RuntimeException("Param file 'param.txt' does not exist.");
        }
        try {
            List<String> s = FileUtils.readLines(paramFile, "UTF-8");

            paramList.addAll(s);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read parameters from  'param.txt'.", ex);
        }

        if (paramList.isEmpty()) {
            throw new RuntimeException("param file is empty.");
        }
        return paramList;
    }

}
