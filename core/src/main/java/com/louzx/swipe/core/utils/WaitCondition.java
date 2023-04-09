package com.louzx.swipe.core.utils;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * http 等待条件
 */
public interface WaitCondition {

    @Deprecated
    void await();

    void await(HttpURLConnection connection, OutputStream os);
}
