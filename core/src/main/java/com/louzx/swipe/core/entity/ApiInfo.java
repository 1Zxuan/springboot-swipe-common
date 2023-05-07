package com.louzx.swipe.core.entity;


import com.louzx.swipe.core.jdbc.Table;
import com.louzx.swipe.core.jdbc.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter@Setter@Table("api_info")
public class ApiInfo {

    private Integer id;
    private Integer projectId;
    private String apiName;
    private String apiUrl;
    private String apiMethod;
    private String apiVerifyType;
    private String closed;
    private String remark;
    private Integer notifyId;
    private String proxyIp;
    private Integer proxyPort;

    @Transient
    private final Map<String, String> header = new HashMap<>();
    @Transient
    private List<ApiVerify> apiVerifies;
    @Transient
    private List<ApiHandler> apiHandlers;
    @Transient
    private List<NotifyTemplate> notifyTemplates;

}
