package com.louzx.swipe.core.constants;

import com.louzx.swipe.core.entity.AbstractSwipeTask;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskPool {

    private final static Map<String, AbstractSwipeTask> TASK_MAP = new ConcurrentHashMap<>(16);

    public static boolean addTask (AbstractSwipeTask swipeTask) {
        if (null == swipeTask || StringUtils.isNotBlank(swipeTask.getId())) {
            return false;
        }
        String id = swipeTask.getId();

        if (TASK_MAP.containsKey(id)) {
            return false;
        }
        synchronized (TASK_MAP) {
            if (!TASK_MAP.containsKey(id)) {
                TASK_MAP.put(id, swipeTask);
                return true;
            }
        }
        return false;
    }

    public static void removeTask(String id) {
        if (StringUtils.isNotBlank(id)) {
            TASK_MAP.remove(id);
        }
    }

    public static void stopTask (String id) {
        if (StringUtils.isNotBlank(id)) {
            AbstractSwipeTask abstractSwipeTask = TASK_MAP.get(id);
            if (null != abstractSwipeTask) {
                abstractSwipeTask.setStop(true);
            }
        }
    }
}
