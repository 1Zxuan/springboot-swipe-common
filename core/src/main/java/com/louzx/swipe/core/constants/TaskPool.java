package com.louzx.swipe.core.constants;

import com.louzx.swipe.core.entity.AbstractSwipeTask;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskPool {

    private final static Map<String, AbstractSwipeTask> TASK_MAP = new ConcurrentHashMap<>(16);

    public static boolean addTask (AbstractSwipeTask swipeTask) {
        String id;
        if (null == swipeTask || StringUtils.isBlank((id = swipeTask.getId()))) {
            return false;
        }

        return swipeTask == TASK_MAP.computeIfAbsent(id, v -> swipeTask);
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

    public static boolean isExist(String id) {
        AbstractSwipeTask abstractSwipeTask = TASK_MAP.get(id);
        if (null != abstractSwipeTask) {
            return abstractSwipeTask.exist();
        }
        return true;
    }

    public static boolean containsKey(String id) {
        return StringUtils.isNotBlank(id) && TASK_MAP.containsKey(id);
    }
}
