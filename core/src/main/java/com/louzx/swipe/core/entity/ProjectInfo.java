package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import com.louzx.swipe.core.jdbc.Transient;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter@Table
public class ProjectInfo {

    private Integer id;
    private String name;
    private String uri;

    @Transient
    private List<ApiInfo> apiInfos;
}
