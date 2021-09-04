package com.louzx.swipe.utils;

import com.alibaba.fastjson.JSONObject;
import com.louzx.swipe.constants.Method;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

/**
 * @author louzx
 * @date 2021/9/3
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final ThreadLocal<List<HttpCookie>> LOCAL_COOKIES = new ThreadLocal<>();

    static {
        try {
            trustAllHttpsCertificates();
        } catch (NoSuchAlgorithmException | KeyManagementException ignore) {  }
    }

    private static Integer defReadTimeOut = 3000;
    private static Integer defConnectionTimeOut = 3000;
    private static Charset defChartSet = StandardCharsets.UTF_8;
    private static String defAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36";
    private static boolean defRandAgent = true;
    private static boolean useCookie = false;


    public static String get (String url) {
        return doHttp(url, Method.GET, null, null, null, null, null, defRandAgent);
    }

    public static JSONObject getJson (String url) {
        return SwipeUtils.parseJson(get(url));
    }



    public static String doHttp (String url, Method method, Map<String, String> header, String body,
                                 Integer readTimeOut, Integer connectionTimeOut, Charset chartSet, boolean randAgent) {
        if (null == chartSet) {
            chartSet = defChartSet;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        GZIPInputStream zipIs = null;
        InputStreamReader isr = null;
        CookieManager cm = null;
        List<HttpCookie> cookies = LOCAL_COOKIES.get();
        try {
//            if (useCookie && null == ) {
//                cm = new CookieManager();
//            }
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
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
            }
            if (200 == conn.getResponseCode()) {
                is = conn.getInputStream();
                isr = new InputStreamReader(is, chartSet);
                br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String tmpStr;
                while (null != (tmpStr = br.readLine())) {
                    sb.append(tmpStr);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            logger.error(">>>>>>>>HttpClient：【{}】，URL：【{}】，Method：【{}】<<<<<<<<", e.getMessage(), url, method);
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

    public static void setUseCookie(boolean useCookie) {
        HttpClientUtils.useCookie = useCookie;
    }
}