package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Table
public class ObjectStuff {
    private Integer id;
    private Integer objectId;
    private String placeHolder;
    private String value;
    private String source;
    private String handlerSource;
    private String handlerType;
    private Integer seq;
}
