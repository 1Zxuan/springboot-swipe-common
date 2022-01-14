package com.louzx.swipe.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class CloseSwipeTask {

    private final Logger logger = LoggerFactory.getLogger(CloseSwipeTask.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CloseSwipeTask(@Qualifier("jdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initTask() {
        jdbcTemplate.update("update swipe_task set `status` = 102 where `status` in (100, 101)" + CommonSwipeConfig.conditionSql());
        jdbcTemplate.update("update swipe_task set `status` = 0 where `status` in (1, 2, 5) " + CommonSwipeConfig.conditionSql());
    }

    @PreDestroy
    public void closeTask() {
        jdbcTemplate.update("update swipe_task set `status` = 102 where `status` in (100, 101)" + CommonSwipeConfig.conditionSql());
        logger.info(">>>>>>>>关闭任务：【{}】个", jdbcTemplate.update("update swipe_task set `status` = 0 where `status` in (1, 2, 5) " + CommonSwipeConfig.conditionSql()));
    }
}
