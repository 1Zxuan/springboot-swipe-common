package com.louzx.swipe.core.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ProcessTask {

    private final Logger logger = LoggerFactory.getLogger(ProcessTask.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProcessTask(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        execute();
    }

    private void execute() {
        logger.info(">处理：【{}】类型的任务", CommonSwipeConfig.getTaskGroup());
        jdbcTemplate.update("update swipe_task t " +
                "inner join user_info t1 on t.username = t1.username " +
                "set t.status = (case when t.status in (100,101) then 102 when t.status in (1, 2, 5) then 0 else t.status end) where 1=1 "
                + CommonSwipeConfig.getTaskGroupCondition());
    }

    @PreDestroy
    public void close() {
        execute();
    }

}
