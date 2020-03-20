package com.linkkou.configproperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 配置获取工具类
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-27 21:26
 */
public class ConfigUtils {

    private Object value;

    private String configvalue;

    private Object defaultValue;

    /**
     * 构造
     *
     * @param configvalue 获取配置Key键
     */
    public ConfigUtils(String configvalue) {
        this.configvalue = configvalue;
    }

    /**
     * 构造
     *
     * @param configvalue  获取配置Key键
     * @param defaultValue 默认值 作用于对Config
     */
    public ConfigUtils(String configvalue, Object defaultValue) {
        this.configvalue = configvalue;
        this.defaultValue = defaultValue;
    }

    /**
     * 获取文本值
     *
     * @return
     */
    public String getString() {
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Integer getInteger() {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        try {
            return Integer.parseInt(getString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Boolean getBoolean() {
        return Boolean.parseBoolean(getString());
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Long getLong() {
        try {
            return Long.parseLong(getString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Double getDouble() {
        try {
            return Double.parseDouble(getString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Float getFloat() {
        try {
            return Float.parseFloat(getString());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Short getShort() {
        try {
            return Short.parseShort(getString());
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取时间
     *
     * @return
     */
    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(getString());
        } catch (ParseException e) {
            date = Date.from(LocalDateTime.of(0000, 00, 00, 00, 00).atZone(ZoneId.systemDefault()).toInstant());
            e.printStackTrace();
        }
        return date;
    }


    /**
     * @param valtype  返回类型
     * @param isConfig 是否是Config接口实现
     * @param <T>
     * @return
     */
    public <T> T getConfig(String valtype, Boolean isConfig) {
        final ConfigImpl config = new ConfigImpl(this, valtype);
        if (isConfig) {
            return (T) config;
        } else {
            return (T) config.get();
        }
    }

    public ConfigUtils setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getConfigvalue() {
        return configvalue;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}