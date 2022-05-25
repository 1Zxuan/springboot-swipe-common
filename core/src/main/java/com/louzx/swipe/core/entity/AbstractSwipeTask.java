package com.louzx.swipe.core.entity;

import com.louzx.swipe.core.constants.CommonConstants;
import com.louzx.swipe.core.utils.SwipeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractSwipeTask {

    public abstract String getId();

    protected volatile boolean stop = false;
    //商品信息
    protected StringBuilder goodsInfo = new StringBuilder();
    //请求体头
    protected Map<String, String> header = new HashMap<>();

    public abstract Integer getSpeed();

    protected List<AbstractTaskItem> taskItems;

    protected Map<String, Integer> goodsMap = new HashMap<>();

    public boolean exist(){
        return SwipeUtils.haveTrue(stop, CommonConstants.SYS_STOP);
    }

}
