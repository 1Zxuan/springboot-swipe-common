package com.louzx.swipe.core.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractTaskItem {

    protected String goodsName;

    public abstract String getGoodsId();
    public abstract Integer getNum();
    public abstract String getId();
}
