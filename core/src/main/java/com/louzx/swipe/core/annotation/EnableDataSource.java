package com.louzx.swipe.core.annotation;

import com.louzx.swipe.core.config.SwipeDataSource;
import com.louzx.swipe.core.config.WxDataSource;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import({SwipeDataSource.class, WxDataSource.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableDataSource {

}
