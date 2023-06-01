package com.louzx.swipe.core.utils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CacheClock {

    //定时任务调度线程池
    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private final static AtomicBoolean INIT_ED = new AtomicBoolean(false);

    //毫秒缓存
    private static volatile long timeMillis;

    public static long currentTimeMillis() {
        return timeMillis;
    }

    public static void start(long period, TimeUnit timeUnit) {
        if (INIT_ED.compareAndSet(false, true)) {
            scheduledExecutorService.scheduleAtFixedRate(() -> CacheClock.timeMillis = System.currentTimeMillis(), 0L, period, timeUnit);
        }
    }

}
