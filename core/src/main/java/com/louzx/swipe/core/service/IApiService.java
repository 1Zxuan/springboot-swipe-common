package com.louzx.swipe.core.service;

import com.louzx.swipe.core.entity.ApiInfo;

import java.util.List;

public interface IApiService {

    List<ApiInfo> loadApi(Integer projectId);

    void loadApiHeader(ApiInfo apiInfo);

    void loadApiVerify(ApiInfo apiInfo);

    void loadApiNotify(ApiInfo apiInfo);
}
