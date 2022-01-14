package com.louzx.swipe.core.thread;


import com.louzx.swipe.core.entity.AbstractSwipeTask;
import com.louzx.swipe.core.service.IPrepareTaskService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

public class PrepareTaskThread implements Runnable {

    @Setter
    private AbstractSwipeTask swipeTask;

    private final IPrepareTaskService prepareTaskService;

    @Autowired
    public PrepareTaskThread(IPrepareTaskService prepareTaskService) {
        this.prepareTaskService = prepareTaskService;
    }

    @Override
    public void run() {
        prepareTaskService.prepare(swipeTask);
    }

}
