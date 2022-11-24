package com.louzx.swipe.core.service.impl;

import com.louzx.swipe.core.constants.CommonConstants;
import com.louzx.swipe.core.service.INotifyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotifyServiceImpl implements INotifyService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    public NotifyServiceImpl(@Qualifier("jdbcTemplateWx") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int notify(String title, String content, String uIds) {

        if (StringUtils.isNotBlank(uIds)) {
            String[] uId = uIds.split(",");
            List<Object[]> args = new ArrayList<>();
            for (String s : uId) {
                args.add(new Object[]{content, title, LocalDateTime.now(), s});
            }
            return Arrays.stream(jdbcTemplate.batchUpdate(CommonConstants.NOTIFY_SQL, args)).sum();
        }

        return 0;
    }
}
