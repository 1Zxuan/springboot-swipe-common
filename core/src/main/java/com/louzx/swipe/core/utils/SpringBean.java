package com.louzx.swipe.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("springIoc")
public class SpringBean implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public Object getBean (String beanName) {
        return context.getBean(beanName);
    }

    public <T> T getBean (Class<T> clazz) {
        return context.getBean(clazz);
    }

    public <T> T getBean (String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }
}
