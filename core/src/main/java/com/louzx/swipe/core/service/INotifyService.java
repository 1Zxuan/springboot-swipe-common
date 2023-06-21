package com.louzx.swipe.core.service;

public interface INotifyService {

    int notify(String title, String content, String uIds);

    boolean notifyNow(String title, String content, String uIds);
}
