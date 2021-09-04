package com.louzx.swipe.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	String value() default "";
	String pks() default "";
	int tableSort() default 0;
	String tableForeignKey() default "";
}
