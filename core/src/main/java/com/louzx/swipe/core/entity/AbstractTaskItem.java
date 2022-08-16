package com.louzx.swipe.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public abstract class AbstractTaskItem {

    protected String goodsName;
    protected AtomicInteger goodsStock;

    public abstract String getGoodsId();
    public abstract Integer getNum();
    public abstract String getId();
    public abstract void setNum(Integer num);
}
