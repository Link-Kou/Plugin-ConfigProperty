package com.linkkou.configproperty;

import com.linkkou.configproperty.local.LocalConfigProperty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ConfigImpl implements Config<Object> {

    /**
     * 所有配置存储,
     * 所有配置在项目启动读一次
     */
    private static Map<String, Object> ctxPropMap = new ConcurrentHashMap<>(16);

    private static Pattern p = Pattern.compile("[\\$\\{\\}]");

    private ConfigUtils configUtils;

    private String valtype;


    private static LocalConfigProperty localConfigProperty = new LocalConfigProperty();


    public ConfigImpl(ConfigUtils value, String valtype) {
        this.configUtils = value;
        this.valtype = valtype;
        getReLoadObject();
    }

    /**
     * 获取配置原始值
     *
     * @return
     */
    public void getReLoadObject() {
        try {
            String matchermsg = p.matcher(this.configUtils.getConfigvalue()).replaceAll("");
            Object value = ctxPropMap.get(matchermsg);
            final Object defaultValue = this.configUtils.getDefaultValue();
            if (value == null && defaultValue != null) {
                value = defaultValue;
            }
            this.configUtils.setValue(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object get() {
        switch (valtype) {
            case "String":
                return configUtils.getString();
            case "Integer":
                return configUtils.getInteger();
            case "Long":
                return configUtils.getLong();
            case "Double":
                return configUtils.getDouble();
            case "Float":
                return configUtils.getFloat();
            case "Short":
                return configUtils.getShort();
            case "Boolean":
                return configUtils.getBoolean();
            case "Date":
                return configUtils.getDate();
            default:
                return null;
        }
    }


    public static Map<String, Object> getCtxPropMap() {
        return ctxPropMap;
    }
}
