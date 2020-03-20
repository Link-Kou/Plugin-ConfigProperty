package com.linkkou.configproperty;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置注解
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-27 21:35
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface ConfigValue {
    /**
     * Spring @Value
     *
     * @return @Value
     */
    Value value();

    /**
     * 默认值;查询不到指定配置的时候采用的值
     *
     * @return null 或 defaultValue
     */
    String defaultValue() default "";
}
