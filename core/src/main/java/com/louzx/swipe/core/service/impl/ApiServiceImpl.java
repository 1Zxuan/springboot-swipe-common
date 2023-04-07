package com.louzx.swipe.core.service.impl;

import com.louzx.swipe.core.constants.CommonConstants;
import com.louzx.swipe.core.dao.ICommonDao;
import com.louzx.swipe.core.entity.ApiHeader;
import com.louzx.swipe.core.entity.ApiInfo;
import com.louzx.swipe.core.entity.ApiVerify;
import com.louzx.swipe.core.entity.NotifyTemplate;
import com.louzx.swipe.core.jdbc.SqlBuilder;
import com.louzx.swipe.core.service.IApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiServiceImpl implements IApiService {

    private final ICommonDao commonDao;

    @Autowired
    public ApiServiceImpl(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }


    @Override
    public List<ApiInfo> loadApi(Integer projectId) {
        List<ApiInfo> apiInfos = commonDao.query(SqlBuilder.build(ApiInfo.class).eq("project_id", projectId).eq("closed", CommonConstants.FALSE));
        for (ApiInfo apiInfo : apiInfos) {
            this.loadApiHeader(apiInfo);
            this.loadApiVerify(apiInfo);
            this.loadApiNotify(apiInfo);
        }
        return apiInfos;
    }

    @Override
    public void loadApiHeader(ApiInfo apiInfo) {
        List<ApiHeader> apiHeader = commonDao.query(SqlBuilder.build(ApiHeader.class)
                .eq("project_id", apiInfo.getProjectId())
                .eq("closed", CommonConstants.FALSE));

        apiHeader.addAll(commonDao.query(SqlBuilder.build(ApiHeader.class)
                .eq("api_id", apiInfo.getId())
                .eq("closed", CommonConstants.FALSE)));
        Map<String, String> header = apiInfo.getHeader();
        for (ApiHeader ah : apiHeader) {
            header.put(ah.getHeaderName(), ah.getHeaderValue());
        }
    }

    @Override
    public void loadApiVerify(ApiInfo apiInfo) {
        apiInfo.setApiVerifies(commonDao.query(SqlBuilder.build(ApiVerify.class)
                .eq("api_id", apiInfo.getId())
                .eq("closed", CommonConstants.FALSE).setOrderBy("seq")));
    }

    @Override
    public void loadApiNotify(ApiInfo apiInfo) {
        apiInfo.setNotifyTemplates(commonDao.query(SqlBuilder.build(NotifyTemplate.class).eq("id", apiInfo.getNotifyId())));
    }
}
