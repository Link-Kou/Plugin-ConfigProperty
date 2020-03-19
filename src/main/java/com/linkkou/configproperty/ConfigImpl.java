package com.linkkou.configproperty;


import java.util.IllegalFormatException;

public class ConfigImpl implements Config<Object> {

    private ConfigUtils value;
    private String valtype;

    public ConfigImpl(ConfigUtils value, String valtype) {
        this.value = value;
        this.valtype = valtype;
    }

    @Override
    public Object get() throws IllegalFormatException {
        Object c = value.getReLoadObject();
        try {
            switch (valtype) {
                case "String":
                    return value.getString();
                case "Integer":
                    return value.getInteger();
                case "Long":
                    return value.getLong();
                case "Double":
                    return value.getDouble();
                case "Float":
                    return value.getFloat();
                case "Short":
                    return value.getShort();
                case "Boolean":
                    return value.getBoolean();
                case "Date":
                    return value.getDate();
                default:
                    throw new IllegalAccessError("Config 不支持" + valtype);
            }
        } catch (Exception e) {
            throw  e;
        }
    }
}
