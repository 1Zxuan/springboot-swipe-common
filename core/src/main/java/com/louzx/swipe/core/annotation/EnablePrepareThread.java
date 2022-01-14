package com.louzx.swipe.core.annotation;

import com.louzx.swipe.core.thread.PrepareTaskThread;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Import(PrepareTaskThread.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnablePrepareThread {
}
