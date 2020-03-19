package com.linkkou.configproperty;

import com.linkkou.configproperty.spring.ConfigMsgPropertyConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 配置获取工具类
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-27 21:26
 */
public class ConfigUtils {

    private static Pattern p = Pattern.compile("[\\$\\{\\}]");

    private Object value;

    private String configvalue;

    public ConfigUtils(String configvalue) {
        this.configvalue = configvalue;
        getReLoadObject();
    }

    /**
     * 获取文本值
     *
     * @return
     */
    public Object getReLoadObject() {
        try {
            String matchermsg = p.matcher(configvalue).replaceAll("");
            return value = ConfigMsgPropertyConfigurer.getCtxProp(matchermsg);
        } catch (Exception e) {
            e.printStackTrace();
        };
        return null;
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
        return Integer.parseInt(getString());
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
        return Long.parseLong(getString());
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Double getDouble() {
        return Double.parseDouble(getString());
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Float getFloat() {
        return Float.parseFloat(getString());
    }

    /**
     * 获取数值
     *
     * @return
     */
    public Short getShort() {
        return Short.parseShort(getString());
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
     * 返回自己
     *
     * @return
     */
    public Config getConfig(String valtype) {
        return new ConfigImpl(this, valtype);
    }

}