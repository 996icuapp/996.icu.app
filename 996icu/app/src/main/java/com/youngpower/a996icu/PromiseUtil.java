package com.youngpower.a996icu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PromiseUtil {

    private static ExecutorService executorService = Executors.newScheduledThreadPool(16);

    private PromiseUtil() {
        throw new AssertionError();
    }

    /**
     * 实现并发同时地对某个action并发执行并返回执行结果
     * 实现思路：
     * 并发创建所有执行的线程，并通过锁(start)阻塞等待着
     * 在创建所有执行的线程后(ready)开始计时，并解锁然所有的线程启动
     * 通过另外一个锁（done）记录执行完的线程
     * 主线程只需关心3点
     * - 所有线程是否准备好
     * - 准备好的话开始计时并解锁开始执行
     * - 等待执行完毕
     *
     * @param callableList 要并发执行的列表
     * @return list 执行结果，list.item为null的话表示执行异常
     * @throws InterruptedException 异常
     */
    public static <T> List<T> all(final List<Callable<T>> callableList) {
        final List<T> result = new ArrayList<>();
        int length = callableList.size();
        final CountDownLatch ready = new CountDownLatch(length);
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch done = new CountDownLatch(length);
        for (final Callable<T> callable : callableList) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ready.countDown();
                    try {
                        start.await();
                        T t = callable.call();
                        result.add(t);
                    } catch (Exception e) {
                        // interrupt when exception
                        Thread.currentThread().interrupt();
                        // set null mean exception
                        result.add(null);
                        e.printStackTrace();
                    } finally {
                        done.countDown();
                    }
                }
            });
        }

        try {
            ready.await();
            start.countDown();
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

}