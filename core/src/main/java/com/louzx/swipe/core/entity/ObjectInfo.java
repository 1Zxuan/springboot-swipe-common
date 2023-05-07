package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import com.louzx.swipe.core.jdbc.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter@Table
public class ObjectInfo {

    private Integer id;
    private String name;
    private String body;

    @Transient
    private List<ObjectStuff> objectStuffs;
}