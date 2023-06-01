package com.louzx.swipe.core.http;

import com.louzx.swipe.core.utils.HttpClientUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

public class HttpClient {

    private static CloseableHttpClient HTTP_CLIENT = null;
    private static PoolingHttpClientConnectionManager CONNECTION_MANAGER = null;

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public synchronized static void init(HttpProperty property) throws NoSuchAlgorithmException, KeyManagementException {
        if (null != HTTP_CLIENT) {
            return;
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTP.id, PlainConnectionSocketFactory.INSTANCE)
                .register(URIScheme.HTTPS.id, new SSLConnectionSocketFactory(HttpClientUtils.sslContext(property.getSslVersion())))
                .build();

        CONNECTION_MANAGER = new PoolingHttpClientConnectionManager(socketFactoryRegistry,
                property.getPoolConcurrencyPolicy(),
                PoolReusePolicy.LIFO,
                TimeValue.NEG_ONE_MILLISECOND,
                property.getSchemePortResolver(),
                property.getDnsResolver(),
                ManagedHttpClientConnectionFactory.INSTANCE
        );

        CONNECTION_MANAGER.setDefaultSocketConfig(property.getSocketConfig());
        CONNECTION_MANAGER.setDefaultConnectionConfig(property.getConnectionConfig());
        CONNECTION_MANAGER.setMaxTotal(property.getMaxTotal());
        CONNECTION_MANAGER.setDefaultMaxPerRoute(property.getMaxPreRoute());

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCircularRedirectsAllowed(false)
                        //从连接池中取出连接的最长等待时间
                        .setConnectionRequestTimeout(property.getConnectionRequestTimeout())
                        .setConnectionKeepAlive(property.getConnectionKeepAlive())
                        .build())
                .setConnectionReuseStrategy(HttpProperty.getConnectionReuseStrategy())
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .setConnectionManager(CONNECTION_MANAGER)
                .setConnectionManagerShared(false)
                .evictExpiredConnections()
                .setRetryStrategy(HttpProperty.getRetryStrategy())
                .disableCookieManagement()
                .disableRedirectHandling()
                .disableAutomaticRetries()
                .setDefaultHeaders(property.getDefaultHeaders());

        if (null != property.getFirstRequestInterceptor()) {
            httpClientBuilder.addRequestInterceptorFirst(property.getFirstRequestInterceptor());
        }

        if (null != property.getRoutePlanner()) {
            httpClientBuilder.setRoutePlanner(property.getRoutePlanner());
        }

        HTTP_CLIENT = httpClientBuilder.build();
    }

    public static void close() {
        if (null != CONNECTION_MANAGER) {
            logger.info("关闭HTTP客户端...");
            CONNECTION_MANAGER.close(CloseMode.IMMEDIATE);
        }
    }

    public static String doHttp(ClassicHttpRequest request) {
        return doHttp(request, HttpClient::readResponse);
    }

    public static <T> T doHttp(ClassicHttpRequest request, final HttpClientResponseHandler<? extends T> responseHandler) {
        if (null != HTTP_CLIENT && null != request) {
            try {
                return HTTP_CLIENT.execute(request, responseHandler);
            } catch (IOException e) {
                logger.error(">请求异常：【{}】", request.getPath(), e);
            }
        }
        return null;
    }

    public static CloseableHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    private static String readResponse(ClassicHttpResponse response) throws IOException {
        if (null == response) {
            return null;
        }

        HttpEntity entity = response.getEntity();
        if (null == entity) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try (InputStream content = entity.getContent();
             InputStreamReader isr = new InputStreamReader(inputStream(content, entity.getContentEncoding()), StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String tmpStr;
            while (null != (tmpStr = br.readLine())) {
                sb.append(tmpStr);
            }
        }

        return sb.toString();
    }

    private static InputStream inputStream(InputStream is, String contentEncoding) throws IOException {
        if ("gzip".equalsIgnoreCase(contentEncoding)) {
            return new GZIPInputStream(is);
        } else {
            return is;
        }
    }
}
