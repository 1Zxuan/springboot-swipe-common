package com.louzx.swipe.core.utils;

import com.jayway.jsonpath.JsonPath;
import com.louzx.swipe.core.constants.CommonConstants;
import com.louzx.swipe.core.entity.ApiInfo;
import com.louzx.swipe.core.entity.ApiVerify;
import org.apache.commons.lang3.StringUtils;

public class ApiUtils {

    public static boolean apiVerify(ApiInfo apiInfo, Object responseObj) throws Exception {
        AssertUtil.isThrow(CommonConstants.FALSE.equals(apiInfo.getAcceptNull()) &&
                null == responseObj, () -> new Exception(apiInfo.getCheckNullFailMsg()));

        if (CommonConstants.TRUE.equals(apiInfo.getApiVerifyType())) {
            AssertUtil.isThrow(null == apiInfo.getApiVerifies(), () -> new Exception(String.format("获取接口：【%s】 校验信息失败", apiInfo.getRemark())));

            for (ApiVerify apiVerify : apiInfo.getApiVerifies()) {
                Object read = JsonPath.read(responseObj, apiVerify.getJsonPath());
                AssertUtil.isThrow(null == read, () -> new Exception(apiVerify.getFailMsg()));
                if (CommonConstants.ZERO.equals(apiVerify.getType()) &&
                        StringUtils.equalsIgnoreCase(String.valueOf(read), apiVerify.getCheckValue())) {
                    return true;
                } else {
                    AssertUtil.isThrow(CommonConstants.ONE.equals(apiVerify.getType()) &&
                            StringUtils.equalsIgnoreCase(String.valueOf(read), apiVerify.getCheckValue()), () -> new Exception(null == apiVerify.getFailMsgJsonPath() ? apiVerify.getFailMsg() : JsonPath.read(responseObj, apiVerify.getFailMsgJsonPath())));
                }
            }
        }
        return false;
    }
}
