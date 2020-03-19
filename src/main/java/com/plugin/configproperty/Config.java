package com.plugin.configproperty;

/**
 * 配置
 * @param <T>
 */
public interface Config<T> {

    /**
     * 默认获取方式
     * @return 泛型
     */
    T get();

}
