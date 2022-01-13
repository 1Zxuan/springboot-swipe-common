package com.louzx.swipe.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.louzx.swipe.core.exception.TaskErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author louzx
 * @date 2021/9/3
 */
public final class SwipeUtils {

    private static final Logger logger = LoggerFactory.getLogger(SwipeUtils.class);

    public static void throwTaskErrorIfTrue(String message, boolean... condition) throws TaskErrorException {
        if (null != condition) {
            for (boolean b : condition) {
                if (b) {
                    throw new TaskErrorException(message);
                }
            }
        }
    }

    public static void sleepToNow(Long sleep) {
        if (null != sleep) {
            long l = sleep - System.currentTimeMillis();
            if (l > 0) {
                sleep(l);
            }
        }
    }

    public static boolean canList (List<?> list) {
        return null != list && list.size() > 0;
    }

    public static boolean haveTrue (boolean... condition) {
        if (null != condition && condition.length > 0) {
            for (boolean b : condition) {
                if (b) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void throwIsNull(Object obj, String message) throws Exception {
        if (null == obj) {
            throw new Exception(message);
        }
    }

    public static <T> void throwIsEmpty (Collection<T> collection, String message) throws Exception {
        if (null == collection || collection.isEmpty()) {
            throw new Exception(message);
        }
    }

    public static void throwIsTrue (boolean condition, String message) throws Exception {
        if (condition) {
            throw new Exception(message);
        }
    }

    public static String jsonConvertFrom (Object data) throws UnsupportedEncodingException {
        return jsonConvertFrom(data, true, null);
    }

    public static String jsonConvertFrom (Object data, boolean encode, Charset charset) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        parse(sb, data);
        if (sb.length() > 0 && sb.indexOf("&") != -1) {
            sb.deleteCharAt(sb.lastIndexOf("&"));
        }
        if (encode && null == charset) {
            charset = StandardCharsets.UTF_8;
        }
        return encode ? URLEncoder.encode(sb.toString(), String.valueOf(charset)) : sb.toString();
    }

    public static void parse (StringBuilder sb, Object data) {
        if (data instanceof Map) {
            JSONObject jo = (JSONObject) data;
            for (Map.Entry<String, Object> entry : jo.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                sb.append(key).append("=");
                if (value instanceof Map) {
                    parse(sb, value);
                } else if (value instanceof List) {
                    JSONArray tmpJa = (JSONArray) value;
                    for (Object o : tmpJa) {
                        parse(sb, o);
                    }
                } else {
                    sb.append(value);
                }
                sb.append("&");
            }
        } else if (data instanceof List) {
            JSONArray ja = (JSONArray) data;
            for (Object o : ja) {
                parse(sb, o);
            }
        } else {
            sb.append(data).append("&");
        }
    }

    /**
     * 当个线程池
     * @return threadPool
     */
    public static ExecutorService singleThreadPool() {
        return new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.MINUTES, new LinkedBlockingDeque<>(Integer.MAX_VALUE));
    }

    public static ExecutorService newThreadPool(int corePoolSize,
                                                int maximumPoolSize,
                                                int maxWait,
                                                TimeUnit timeUnit,
                                                BlockingQueue<Runnable> queue) {
        if (corePoolSize < 0 || maxWait < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize) {
            return null;
        }
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, maxWait, timeUnit, queue);
    }


    /**
     *
     * @param str 需要被截取的文字
     * @param prefix 起始位字符
     * @param end 结束字符
     * @return res 截取完成字符串
     */
    public static String subStr (String str, String prefix, String end) {
        String res = null;
        int s = str.indexOf(prefix);
        int e = str.indexOf(end);
        if (s > 0 && e > 0) {
            res = str.substring(s + prefix.length(), e);
        }
        return res;
    }

    /***
     * 格式化返回数据
     * @param str 需要格式化文字
     * @return json
     */
    public static JSONObject parseJson (String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            return JSONObject.parseObject(str);
        } catch (Exception e) {
            logger.error(">>>>>>>>获取JSON数据异常：【{}】<<<<<<<<", e.getMessage());
            return null;
        }
    }

    public static String randAgent() {
        return AGENTS[timeRandNum(AGENTS.length)];
    }

    /**
     *  0 ~ max  不包含max
     * @param max 最大值
     * @return 随机数
     */
    public static int timeRandNum (int max) {
        return (int) (System.currentTimeMillis() % max);
    }

    /**
     *  0 ~ max  不包含max
     * @param max 最大值
     * @return 随机数
     */
    public static int mathRandNum (int max) {
        return (int) (Math.random() * max);
    }

    public static void sleepFix(long sleepTime) {
        sleep(sleepTime);
    }

    public static void sleepIfTrue(boolean condition, Integer sleepTime) {
        sleepIfTrue(condition, sleepTime, false);
    }

    public static void sleepRandIfTrue (boolean condition, Integer sleepTime){
        if (condition) {
            randSleep(sleepTime);
        }
    }

    public static void sleepIfTrue(boolean condition, Integer sleepTime, boolean rand) {
        if (condition) {
            if (rand) {
                randSleep(sleepTime);
            } else {
                sleep(sleepTime);
            }
        }
    }

    public static void sleepIfNull(Object obj) {
        sleepIfNull(obj, 100, true);
    }

    public static void sleepIfNull(Object obj, long sleepTime) {
        sleepIfNull(obj, sleepTime, true);
    }

    public static void sleepIfNull(Object obj, long sleepTime, boolean rand) {
        if (null == obj) {
            if (rand) {
                randSleep(sleepTime);
            } else {
                sleepFix(sleepTime);
            }
        }
    }

    public static void randSleep(long sleepTime) {
        sleep((long) (Math.random() * sleepTime));
    }

    public static void sleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (Exception ignore) {
        }
    }

    /**
     * user-agent 集合
     */
    public static final String[] AGENTS = new String[]
            {
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
                    "Opera/8.0 (Windows NT 5.1; U; en)",
                    "Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
                    "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
                    "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11",
                    "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
                    "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
                    "Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                    "MAC：Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36",
                    "Windows：Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
                    "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
                    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SE 2.X MetaSr 1.0; SE 2.X MetaSr 1.0; .NET CLR 2.0.50727; SE 2.X MetaSr 1.0)",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.3.4000 Chrome/30.0.1599.101 Safari/537.36",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 UBrowser/6.2.4094.1 Safari/537.36",
                    "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (iPod; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
                    "Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5",
                    "Mozilla/5.0 (Linux; U; Android 2.2.1; zh-cn; HTC_Wildfire_A3333 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
                    "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
                    "MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
                    "Opera/9.80 (Android 2.3.4; Linux; Opera Mobi/build-1107180945; U; en-GB) Presto/2.8.149 Version/11.10",
                    "Mozilla/5.0 (Linux; U; Android 3.0; en-us; Xoom Build/HRI39) AppleWebKit/534.13 (KHTML, like Gecko) Version/4.0 Safari/534.13",
                    "Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en) AppleWebKit/534.1+ (KHTML, like Gecko) Version/6.0.0.337 Mobile Safari/534.1+",
                    "Mozilla/5.0 (hp-tablet; Linux; hpwOS/3.0.0; U; en-US) AppleWebKit/534.6 (KHTML, like Gecko) wOSBrowser/233.70 Safari/534.6 TouchPad/1.0",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; The World)",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",
                    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Avant Browser)",
                    "Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
                    "Mozilla/5.0 (SymbianOS/9.4; Series60/5.0 NokiaN97-1/20.0.019; Profile/MIDP-2.1 Configuration/CLDC-1.1) AppleWebKit/525 (KHTML, like Gecko) BrowserNG/7.1.18124",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0; HTC; Titan)",
                    "UCWEB7.0.2.37/28/999",
                    "NOKIA5700/ UCWEB7.0.2.37/28/999",
                    "Openwave/ UCWEB7.0.2.37/28/999",
                    "Openwave/ UCWEB7.0.2.37/28/999"
            };
}
