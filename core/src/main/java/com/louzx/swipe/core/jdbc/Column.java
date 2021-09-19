package com.louzx.swipe.core.jdbc;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 
public @interface Column {
	
	String value();
}
