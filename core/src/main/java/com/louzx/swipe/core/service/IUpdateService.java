package com.louzx.swipe.core.service;

import com.louzx.swipe.core.jdbc.SqlBuilder;

/**
 * @description 异步更新数据库导致数据库异常
 * */
@Deprecated
public interface IUpdateService {

    void update(SqlBuilder sqlBuilder);
}
