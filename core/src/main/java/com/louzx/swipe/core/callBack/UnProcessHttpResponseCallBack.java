package com.louzx.swipe.core.callBack;

import com.louzx.swipe.core.utils.HttpClientUtils;

public final class UnProcessHttpResponseCallBack implements HttpClientUtils.CallBack {

    private static ThreadLocal<Integer> LOCAL_CODE = null;

    private static volatile UnProcessHttpResponseCallBack INSTANCE = null;

    private UnProcessHttpResponseCallBack() {
        LOCAL_CODE = new ThreadLocal<>();
    }

    @Override
    public boolean responseCode(Integer code) {
        LOCAL_CODE.set(code);
        return false;
    }

    public static Integer responseCode() {
        try {
            return null == LOCAL_CODE ? null : LOCAL_CODE.get();
        } finally {
            if (null != LOCAL_CODE) {
                LOCAL_CODE.remove();
            }
        }
    }

    public static boolean checkCode(int acceptCode) {
        Integer code = responseCode();
        return null != code && code.compareTo(acceptCode) == 0;
    }

    public static UnProcessHttpResponseCallBack getInstance() {

        if (null == INSTANCE) {
            synchronized (UnProcessHttpResponseCallBack.class) {
                if (null == INSTANCE) {
                    INSTANCE = new UnProcessHttpResponseCallBack();
                }
            }
        }

        return INSTANCE;
    }

}
