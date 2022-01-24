package com.zqf.custom.tomcat.thread;

import java.util.concurrent.*;

public class ThreadPool {

    int corePoolSize = 10;
    int maximumPoolSize = 50;
    long keepAliveTime = 100L;
    TimeUnit unit = TimeUnit.SECONDS;
    //阻塞队列
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
    //线程工厂
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    //拒绝策略
    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    public ThreadPoolExecutor getThreadPoolExecutor() {

        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler
        );
    }
}
