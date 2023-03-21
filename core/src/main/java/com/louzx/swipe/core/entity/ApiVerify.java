package com.louzx.swipe.core.entity;


import com.louzx.swipe.core.jdbc.Table;
import com.louzx.swipe.core.jdbc.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table
public class ApiVerify {

    private Integer id;
    private Integer apiId;
    private String type;
    private String jsonPath;
    private String checkValue;
    private String failMsgJsonPath;
    private String failMsg;
    private Integer seq;
    private Integer parentId;
    private Integer notifyId;
    private String closed;

    @Transient
    private List<ApiVerify> childVerifies;

}
