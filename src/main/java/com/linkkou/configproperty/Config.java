package com.linkkou.configproperty;

/**
 * 配置，获取到配置类
 * @param <T>
 */
public interface Config<T> {

    /**
     * 默认获取方式
     * @return 泛型
     */
    T get();

}
