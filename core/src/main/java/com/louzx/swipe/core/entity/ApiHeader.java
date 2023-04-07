package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.jdbc.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table
public class ApiHeader {

    private String headerName;
    private String headerValue;
    private String valueSource;

}
