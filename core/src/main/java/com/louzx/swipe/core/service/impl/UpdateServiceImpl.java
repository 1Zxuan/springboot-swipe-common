package com.louzx.swipe.core.service.impl;

import com.louzx.swipe.core.dao.ICommonDao;
import com.louzx.swipe.core.jdbc.SqlBuilder;
import com.louzx.swipe.core.service.IUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements IUpdateService {

    private final ICommonDao commonDao;

    @Autowired
    public UpdateServiceImpl(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }

    @Async
    @Override
    public void update(SqlBuilder sqlBuilder) {
        commonDao.updateByProperty(sqlBuilder);
    }
}
