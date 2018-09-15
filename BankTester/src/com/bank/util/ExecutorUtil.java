package com.bank.util;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程执行器工具类
 */
public class ExecutorUtil {

    /**
     * 创建固定大小线程池
     * 
     * @param size 线程池大小
     * @param name 线程池名称
     * @return
     */
    public static final ExecutorService createFixedThreadPool(int size, String name) {
        return Executors.newFixedThreadPool(size, new DaemonThreadFactory(name));
    }

    /**
     * @param name
     * @return
     */
    public static final ExecutorService createCachedThreadPool(String name) {
        return Executors.newCachedThreadPool(new DaemonThreadFactory(name));

    }

    /**
     * Daemon线程创建工厂
     */
    public static class DaemonThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final UncaughtExceptionHandler exceptionHandler;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public DaemonThreadFactory() {
            this("thread");
        }

        public DaemonThreadFactory(String threadPrefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            exceptionHandler = new PrintUncaughtExceptionHandler();
            namePrefix = "Thread-" + poolNumber.getAndIncrement() + "-" + threadPrefix + "-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setUncaughtExceptionHandler(exceptionHandler);
            if (!t.isDaemon()) {
                t.setDaemon(true);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    /**
     * 线程异常处理
     */
    private static class PrintUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得默认的线程池大小，CPU数目+1
     * 
     * @return
     */
    public static int defaultCorePoolSize() {
        int core = Runtime.getRuntime().availableProcessors() + 1;
        return core;
    }
}
