package com.louzx.swipe.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("springIoc")
public class SpringBean implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBean.context = applicationContext;
    }

    public static Object getBean (String beanName) {
        return context.getBean(beanName);
    }

    public static <T> T getBean (Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean (String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }
}
