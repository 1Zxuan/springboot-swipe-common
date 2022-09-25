package com.louzx.swipe.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.louzx.swipe.core.constants.Method;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author louzx
 * @date 2021/9/3
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    static {
        try {
            trustAllHttpsCertificates();
        } catch (NoSuchAlgorithmException | KeyManagementException ignore) {  }
    }

    private static boolean logOut = false;
    private static Integer defReadTimeOut = 3000;
    private static Integer defConnectionTimeOut = 3000;
    private static Charset defChartSet = StandardCharsets.UTF_8;
    private static String defAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
    private static boolean defRandAgent = true;
    /** 是否开启输出 */
    private static final ThreadLocal<Boolean> throughoutOpenOutStream = new ThreadLocal<>();
    /** 代理 */
    private static Proxy proxy;

    public static String get (String url) {
        return get(url, null);
    }

    public static String get (String url, Integer connectionTimeOut, Integer readTimeOut) {
        return doHttp(url, Method.GET, null, null, readTimeOut, connectionTimeOut, null, defRandAgent, null, null);
    }

    public static String get (String url, CallBack callBack) {
        return doHttp(url, Method.GET, null, null, null, null, null, defRandAgent, callBack, null);
    }

    public static String doHttp (String url, Method method, Map<String, String> header, String body) {
        return doHttp(url, method,header, body, null, null);
    }

    public static String doHttp(String url, Method method, Map<String, String> header, String body, WaitCondition waitCondition) {
        return doHttp(url, method, header, body, defReadTimeOut, defConnectionTimeOut, StandardCharsets.UTF_8, defRandAgent, null, waitCondition);
    }

    public static String doHttp (String url, Method method, Map<String, String> header, String body, CallBack callBack, WaitCondition waitCondition) {
        return doHttp(url, method, header, body, defReadTimeOut, defConnectionTimeOut, StandardCharsets.UTF_8, defRandAgent, callBack, waitCondition);
    }

    public static JSONObject getJson (String url, Method method, Map<String, String> header, String body) {
        return SwipeUtils.parseJson(doHttp(url, method, header, body, defReadTimeOut, defConnectionTimeOut, StandardCharsets.UTF_8, defRandAgent, null, null));
    }

    public interface CallBack {
        default String doCallBack(String response) {
            return response;
        }

        default boolean responseCode(Integer code) {
            return true;
        }
    }

    public static String doHttp (String url, Method method, Map<String, String> header, String body,
                                 Integer readTimeOut, Integer connectionTimeOut, Charset chartSet,
                                 boolean randAgent, CallBack callBack, WaitCondition waitCondition) {
        if (null == chartSet) {
            chartSet = defChartSet;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        GZIPInputStream zipIs = null;
        InputStreamReader isr = null;
        StringBuilder sb = null;
        try {
            URL u = new URL(url);
            conn = null == proxy ? (HttpURLConnection) u.openConnection() : (HttpURLConnection) u.openConnection(proxy);
            conn.setRequestMethod(null == method ? Method.GET.name() : method.name());
            conn.setConnectTimeout(null == connectionTimeOut ? defConnectionTimeOut : connectionTimeOut);
            conn.setReadTimeout(null == readTimeOut ? defReadTimeOut : readTimeOut);
            conn.setRequestProperty("User-Agent", randAgent ? SwipeUtils.randAgent() : defAgent);
            if (null != header && header.size() > 0) {
                header.forEach(conn::setRequestProperty);
            }
            conn.setDoInput(true);
            if (StringUtils.isNotBlank(body)) {
                conn.setDoOutput(true);
                os = conn.getOutputStream();
                os.write(body.getBytes(chartSet));
            } else if (null != throughoutOpenOutStream.get() && throughoutOpenOutStream.get()) {
                conn.setDoOutput(true);
                os = conn.getOutputStream();
                os.write("".getBytes(chartSet));
            }
            if (null != waitCondition) {
                waitCondition.await();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode >= 400) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }
            if (null != callBack) {
                boolean isContinue = callBack.responseCode(responseCode);
                if (!isContinue) {
                    return null;
                }
            }
            if (null != is) {
                String contentEncoding = conn.getContentEncoding();
                if ("gzip".equalsIgnoreCase(contentEncoding)) {
                    zipIs = new GZIPInputStream(is);
                    isr = new InputStreamReader(zipIs, chartSet);
                } else {
                    isr = new InputStreamReader(is, chartSet);
                }
                sb = new StringBuilder();
                br = new BufferedReader(isr);
                String tmpStr;
                while (null != (tmpStr = br.readLine())) {
                    sb.append(tmpStr);
                }
                if (null != callBack) {
                    return callBack.doCallBack(sb.toString());
                }
                return sb.toString();
            }
        } catch (Exception e) {
            if (logOut) {
                logger.error(">>>>>>>>HttpClient：【{}】，URL：【{}】，Method：【{}】<<<<<<<<", e.getMessage(), url, method);
            }
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(zipIs);
            IOUtils.closeQuietly(isr);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
            IOUtils.close(conn);
        }
        return null;
    }

    private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[1];
        trustManagers[0] = new TrustAllManager();
        SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
        sslContext.getServerSessionContext().setSessionCacheSize(0);
        sslContext.init(null, trustManagers, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    public static void trustAllHttpsCertificates(String ssLVersion) throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[1];
        trustManagers[0] = new TrustAllManager();
        SSLContext sslContext = SSLContext.getInstance(ssLVersion);
        sslContext.getServerSessionContext().setSessionCacheSize(0);
        sslContext.init(null, trustManagers, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    /* ******** Set Methods ********/

    public static void setDefReadTimeOut(Integer defReadTimeOut) {
        HttpClientUtils.defReadTimeOut = defReadTimeOut;
    }

    public static void setDefConnectionTimeOut(Integer defConnectionTimeOut) {
        HttpClientUtils.defConnectionTimeOut = defConnectionTimeOut;
    }

    public static void setDefChartSet(Charset defChartSet) {
        HttpClientUtils.defChartSet = defChartSet;
    }

    public static void setDefAgent(String defAgent) {
        HttpClientUtils.defAgent = defAgent;
    }

    public static void setDefRandAgent(boolean defRandAgent) {
        HttpClientUtils.defRandAgent = defRandAgent;
    }

    public static void setThroughoutOpenOutStream(boolean open) {
        throughoutOpenOutStream.set(open);
    }

    public static void removeThroughoutOpenOutStream() {
        throughoutOpenOutStream.remove();
    }

    public static void setProxy(Proxy proxy) {
        HttpClientUtils.proxy = proxy;
    }

    public static void setLogOut(boolean logOut) {
        HttpClientUtils.logOut = logOut;
    }
}
