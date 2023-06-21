package com.louzx.swipe.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.louzx.swipe.core.constants.CommonConstants;
import com.louzx.swipe.core.constants.Method;
import com.louzx.swipe.core.service.INotifyService;
import com.louzx.swipe.core.utils.HttpClientUtils;
import com.louzx.swipe.core.utils.SwipeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NotifyServiceImpl implements INotifyService {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, String> header = new HashMap<String, String>() {{
        put("Content-Type", "application/json");
        put("Host", "wxpusher.zjiecode.com");
        put("Accept", "*/*");
        put("Accept-Encoding", "gzip, deflate, br");
        put("Connection", "close");
    }};

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

    @Override
    public boolean notifyNow(String title, String content, String uIds) {
        JSONObject body = new JSONObject();
        body.put("appToken", "AT_1Yx3TUzemrfzpR2zyomSrd2eoXPqouDK");
        body.put("contentType", "2");
        body.put("topicIds", Collections.singleton("3006"));
        body.put("content", content);
        body.put("summary", title);
        body.put("uids", uIds.split(","));
        String response = HttpClientUtils.doHttp("https://wxpusher.zjiecode.com/api/send/message", Method.POST, header, body.toString());
        JSONObject jsonObject = SwipeUtils.parseJson(response);
        return null != jsonObject && jsonObject.getBooleanValue("success");
    }
}
