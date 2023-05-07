package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Table
public class ApiHandler {

    private Integer id;
    private Integer apiId;
    private String name;
    private String jsonPath;
    //0长期有效；1使用后失效
    private String expiredType;
    //0直接取值；1取值后处理
    private String processType;

}
