package com.louzx.swipe.core.http;

import lombok.*;
import org.apache.hc.client5.http.DnsResolver;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.SchemePortResolver;
import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.ConnectionReuseStrategy;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpRequestInterceptor;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpProperty {

    private String sslVersion = "TLSv1.2";

    private PoolConcurrencyPolicy poolConcurrencyPolicy = PoolConcurrencyPolicy.LAX;

    private PoolReusePolicy poolReusePolicy = PoolReusePolicy.LIFO;

    private SchemePortResolver schemePortResolver = DefaultSchemePortResolver.INSTANCE;

    private DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

    private SocketConfig socketConfig = SocketConfig.custom()
            .setSoTimeout(30, TimeUnit.SECONDS)
            .setTcpNoDelay(true)
            .setSoKeepAlive(true)
            .build();

    private ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(2, TimeUnit.SECONDS)
            //validateAfterInactivity 空闲永久连接检查间隔，这个牵扯的还比较多
            //官方推荐使用这个来检查永久链接的可用性，而不推荐每次请求的时候才去检查
            .setValidateAfterInactivity(TimeValue.NEG_ONE_MILLISECOND)
            .setTimeToLive(TimeValue.ofMinutes(5))
            .build();

    //设置连接池的最大连接数，针对连接池
    private int maxTotal = 300;
    //
    private int maxPreRoute = 300;

    private final List<Header> defaultHeaders = new ArrayList<>();

    //请求拦截器
    private HttpRequestInterceptor firstRequestInterceptor;

    //代理
    private HttpRoutePlanner routePlanner;

    private Timeout connectionRequestTimeout = TimeValue.ofMilliseconds(50).toTimeout();

    private TimeValue connectionKeepAlive = TimeValue.ofMinutes(2);

    // 重定向策略
    private static HttpRequestRetryStrategy retryStrategy = DefaultHttpRequestRetryStrategy.INSTANCE;

    private static ConnectionReuseStrategy connectionReuseStrategy = DefaultConnectionReuseStrategy.INSTANCE;

    public static ConnectionReuseStrategy getConnectionReuseStrategy() {
        return connectionReuseStrategy;
    }

    public static HttpRequestRetryStrategy getRetryStrategy() {
        return retryStrategy;
    }

    public static void setRetryStrategy(HttpRequestRetryStrategy retryStrategy) {
        HttpProperty.retryStrategy = retryStrategy;
    }

    public static void setConnectionReuseStrategy(ConnectionReuseStrategy connectionReuseStrategy) {
        HttpProperty.connectionReuseStrategy = connectionReuseStrategy;
    }
}
