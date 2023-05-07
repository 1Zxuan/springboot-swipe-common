package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Table
public class ApiParam {

    private Integer id;
    private Integer projectId;
    private Integer apiId;
    //0:路径变量；1:路径参数；2：请求体；3：请求头
    private String type;
    private String name;
    private String value;
    //值来源：0固定值、1全局变量取值、2静态方法取值、3对象填充
    private String source;

}
