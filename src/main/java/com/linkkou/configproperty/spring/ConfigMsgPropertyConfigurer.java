package com.linkkou.configproperty.spring;


import com.linkkou.configproperty.ConfigImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 获取到当前架包中资源文件夹下的Properties配置文件
 * Spring接口实现
 *
 * @author LK
 * @version 1.0
 * @data 2017-12-10 21:47
 */
public class ConfigMsgPropertyConfigurer extends PropertyPlaceholderConfigurer {


    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            ConfigImpl.getCtxPropMap().put(keyStr, props.get(keyStr));
        }
    }

}