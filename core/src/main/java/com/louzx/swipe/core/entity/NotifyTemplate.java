package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table
public class NotifyTemplate {

    private Integer id;
    private String msgContent;
    private String uid;

}
