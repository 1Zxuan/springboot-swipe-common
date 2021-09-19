package com.louzx.swipe.core.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author 1Zx.
 * @date 2020/4/20 11:46
 */
@Repository("bootBaseDao")
public class BaseDao {

    @Resource
    public JdbcTemplate jdbcTemplate;

    @Resource
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate;

}
