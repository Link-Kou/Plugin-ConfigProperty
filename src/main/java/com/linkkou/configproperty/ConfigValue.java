package com.linkkou.configproperty;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
/**
 * 注解
 * @author LK
 * @version 1.0
 * @data 2017-12-27 21:35
 */
public @interface ConfigValue {
    Value value();
}
